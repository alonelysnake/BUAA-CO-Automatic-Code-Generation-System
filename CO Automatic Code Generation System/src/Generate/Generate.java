package Generate;

import Instruction.I.*;
import Instruction.I.IBranch.*;
import Instruction.I.ICal.*;
import Instruction.I.ILS.*;
import Instruction.J.*;
import Instruction.R.*;
import Exception.UnknownInstrException;

import java.util.*;

public class Generate
{
    private int instructionNum;//指令条数
    private int cnt = 0;//当前指令index
    final private int labelBlockSize = 3;//label块大小
    private Set<String> RCal;//选择的R指令
    private Set<String> ICal;//选择的I指令（不分支）
    private Set<String> RJ;//jalr,jr
    private Set<String> LS;//load/save指令
    private Set<String> J;//实现jump的指令（j，jal，jalr，jr）
    private Set<String> branch;//I中的分支指令
    private Set<String> MD;//乘除法相关

    final private int SEED = 114514;//神奇的随机数种子
    Random random = new Random(SEED);

    RInstruction rInstruction;
    IInstruction iInstruction;
    JInstruction jInstruction;

    Set<Integer> writeProhibit;//不可以做运算写入的寄存器（系统用和jr/jalr用的）
    HashSet<Integer> hasVal = new HashSet<>();//有值的寄存器
    List<Integer> addrList = new ArrayList<>();//有值的存储器地址
    List<String> labelList = new ArrayList<>();//存储分支指令的出现顺序
    LinkedList<Integer> conflictReg = new LinkedList<>();//记录会发生冲突的寄存器

    List<String> ans = new ArrayList<>();//输出的字符串数组

    public Generate()
    {
        Init init = new Init();
        this.RCal = init.RCal;
        this.ICal = init.ICal;
        this.branch = init.branch;
        this.RJ = init.RJ;
        this.J = init.J;
        this.LS = init.LS;
        this.MD = init.MD;
        this.writeProhibit = init.writeProhibit;
    }

    //grf与dm赋初值
    private void initReg()
    {
        //寄存器赋初始值
        this.hasVal.add(0);
        this.hasVal.add(2);
        this.hasVal.add(3);
        //$v0($2)值固定为2^32-1
        this.ans.add("lui $v0, 0x7fff");
        this.ans.add("ori $v0, $v0, 0xffff");
        //$v1($3)值固定为-2^32
        this.ans.add("lui $v1, 0xffff");
        this.ans.add("ori $v1, $v1, 0xffff");

        //随机几个寄存器赋值
        for (int i = 0; i < 3; i++)
        {
            iInstruction = new Ori(writeProhibit, this.hasVal, this.conflictReg);
            this.ans.add(iInstruction.createMIPSText());
            //增加有值的寄存器
            //hasVal.add(iInstruction.getRt());
            this.cnt++;
        }
        //存储部分值（方便后续取指令测试）
        for (int i = 0; i < 3; i++)
        {
            this.iInstruction = new Sw(this.hasVal, this.addrList);
            this.ans.add(iInstruction.createMIPSText());
            //this.addrList.add(Integer.valueOf(iInstruction.getImm16()));
            this.cnt++;
        }
    }

    //创建最后的label部分
    private void createLabel()
    {
        //以下完成label块
        int size = this.labelList.size() * 2;//label块的数目
        for (int i = 0; i < size; i++)
        {
            //先输入label 'i' :
            String text = "label" + i + ":";
            ans.add(text);

            //再生成固定数目的随机非分支跳转指令
            createRandomBlock();

            //最后根据跳转过来的指令类型决定后续跳转位置
            //不同分支跳转指令不同处理
            String instr = this.labelList.get(i / 2);
            if (instr.equals("jal"))
            {
                //一定概率执行jalr（如果要测试）
                boolean jalr = this.random.nextBoolean();
                if (this.RCal.contains("jalr") && jalr)
                {
                    //未实现
                    System.out.println("未实现");
                } else
                {
                    //可以考虑ori给其他寄存器然后jr那个
                    //ori
                    iInstruction = new Ori(this.writeProhibit, this.conflictReg, 31, "0");
                    ans.add(iInstruction.getText());
                    //jr
                    rInstruction = new Jr(iInstruction.getRt());
                    ans.add(rInstruction.getText());
                }
                //延迟槽
                createDelaySlot();
                //改变临时保存地址返回值的寄存器为可写入（必须保证上面这个延迟槽不会用到这个寄存器）
                int rs = rInstruction.getRs();
                if (rs != 1 && rs != 31)
                {
                    this.writeProhibit.remove(rs);
                }
            } else
            {
                //跳转的标签值
                //根据模板实行统一的跳转方式（进3退1）
                String label;
                if (i % 2 == 0)
                {
                    //label+3
                    label = "label" + (i + 3);
                } else
                {
                    //label-1
                    label = "label" + (i - 1);
                }

                if (instr.equals("j"))
                {
                    jInstruction = new J(label);
                    ans.add(jInstruction.createMIPSText());
                } else if (this.branch.contains(instr))
                {
                    //beq，bne，bgez这种
                    switch (instr)
                    {
                        case "beq":
                            iInstruction = new Beq(label, this.conflictReg);
                            break;
                        case "bne":
                            break;
                    }

                    ans.add(iInstruction.createMIPSText());
                } else
                {
                    System.out.println(" Σ( ° △ °|||)︴  又在分支跳转里放奇怪的指令了");
                    System.out.println("错误指令：" + instr);
                    jInstruction = new J("end");
                    ans.add(jInstruction.getText());
                }
                //延迟槽
                createDelaySlot();
            }
        }

        //最后必须有的label
        ans.add("label" + (size + 1) + ":");
    }

    //随机创建label块内的指令
    private void createRandomBlock()
    {
        for (int i = 0; i < this.labelBlockSize; i++)
        {
            int mode = this.random.nextInt(2);
            if (mode == 0)
            {
                //R指令
                try
                {
                    addRCal();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            } else
            {
                //I指令
                try
                {
                    addICal();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            }
        }
    }

    //添加延迟槽
    private void createDelaySlot()
    {
        boolean add = false;
        while (!add)
        {
            try
            {
                addRCal();
                add = true;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    private void createMulDivBlock()
    {
        //创建时保证乘除法之前先给被乘数赋值，尤其是除数必须不为0！！！
        int[] cal = new int[2];
        for (int i = 0; i < 2; i++)
        {
            iInstruction = new Lui(writeProhibit, hasVal, conflictReg);
            this.ans.add(iInstruction.getText());
            int rs = iInstruction.getRs();
            String imm = Integer.toHexString(random.nextInt(65535));
            iInstruction = new Ori(writeProhibit, conflictReg, rs, imm);
            this.ans.add(iInstruction.getText());
            cal[i] = iInstruction.getRt();
        }
        try
        {
            //添加乘除法运算
            addMD(cal[0], cal[1]);
            //乘除槽
            for (int i = random.nextInt(5); i >= 0; i--)
            {
                createDelaySlot();
            }
            //冲突指令
            addMD(0, 0);
        } catch (UnknownInstrException e)
        {
            e.printError();
        }
    }

    public int getInstructionNum()
    {
        return instructionNum;
    }

    public Set<String> getRCal()
    {
        return RCal;
    }

    public Set<String> getICal()
    {
        return ICal;
    }

    public Set<String> getJ()
    {
        return J;
    }

    public void setInstructionNum(int instructionNum)
    {
        this.instructionNum = instructionNum;
    }

    public void setRCal(Set<String> RCal)
    {
        this.RCal = RCal;
    }

    public void setICal(Set<String> ICal)
    {
        this.ICal = ICal;
    }

    public void setBranch(Set<String> branch)
    {
        this.branch = branch;
    }

    public void setJ(Set<String> j)
    {
        this.J = j;
    }

    //程序运行
    public void run()
    {
        int RNum = 10;//R指令数目
        int JNum = 2;//J型（jal）指令数目
        int INum = 15;//I指令数目
        int LSNum = 10;
        int branchNum = 4;
        int RINum = 15;//RI混合

        //以上是寄存器与存储器的初始化部分
        this.initReg();

        //J指令（jal）
        testJ(JNum);
        //R指令内部测试
        testRCal(RNum);
        //I指令（无跳转的）内部测试
        testICal(INum);
        //LS内部测试
        testLS(LSNum);
        //随机生成RI序列
        testRI(RINum);
        //I指令（分支跳转的）
        testBranch(branchNum);
        //结束
        if (this.J.contains("j"))
        {
            this.jInstruction = new J(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("j");
            //延迟槽
            createDelaySlot();
        }

        //至此所有不涉及跳转分支的都应该运行完了，会保证通过end标签直接指向程序终止
        this.ans.add("beq $0, $0, end");
        //延迟槽
        createDelaySlot();

        //创建最后的label部分
        this.createLabel();

        //保证程序运行结束
        this.ans.add("end:");
        FileGenerate fileGenerate = new FileGenerate(this.ans);
        fileGenerate.update();
    }

    private void addRCal() throws UnknownInstrException
    {
        String newInstr;

        this.rInstruction = InstrChoose.chooseRCal(RCal, this.writeProhibit, this.hasVal, this.conflictReg);
        newInstr = this.rInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("添加 " + instruction + " 指令成功");
    }

    //增加非分支的I指令
    private void addICal() throws UnknownInstrException
    {
        String newInstr;

        this.iInstruction = InstrChoose.chooseICal(ICal, writeProhibit, hasVal, conflictReg);
        newInstr = iInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("添加 " + instruction + " 指令成功");
    }

    private void addLS() throws UnknownInstrException
    {

        String newInstr;

        iInstruction = InstrChoose.chooseLS(LS, addrList, writeProhibit, hasVal, conflictReg);
        newInstr = iInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("添加 " + instruction + " 指令成功");
    }

    //随机增加I指令中产生分支的指令
    private void addBranch() throws UnknownInstrException
    {
        String newInstr;

        iInstruction = InstrChoose.chooseBranch(branch, labelList, conflictReg);
        newInstr = iInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
    }

    private void addMD(int rs, int rt) throws UnknownInstrException
    {
        String newInstr;

        rInstruction = InstrChoose.chooseMD(MD, rs, rt, writeProhibit, hasVal, conflictReg);
        newInstr = rInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
    }

    void testRCal(int RNum)
    {
        for (int i = 0; i < RNum; i++)
        {
            try
            {
                addRCal();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    void testICal(int INum)
    {
        for (int i = 0; i < INum; i++)
        {
            try
            {
                addICal();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    void testLS(int LSNum)
    {
        for (int i = 0; i < LSNum; i++)
        {
            try
            {
                addLS();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    void testMD(int MDNum)
    {
        for (int i = 0; i < MDNum; i++)
        {
            createMulDivBlock();
        }
    }

    void testJ(int JNum)
    {
        for (int i = 0; i < JNum; i++)
        {
            jInstruction = new Jal(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("jal");
            this.createDelaySlot();//延迟槽
        }
    }

    void testRI(int RINum)
    {
        for (int i = 0; i < RINum; i++)
        {
            int num = random.nextInt(2);
            //随机顺序测试不同指令块间的阻塞
            boolean[] block = {true, true, true, true};
            int cnt = 4;
            while (cnt > 0)
            {
                int index = random.nextInt(4);
                while (!block[index])
                {
                    index = random.nextInt(4);
                }
                switch (index)
                {
                    case 0:
                        testRCal(num);
                        break;
                    case 1:
                        testICal(num);
                        break;
                    case 2:
                        testLS(num);
                        break;
                    case 3:
                        testMD(num);
                        break;
                }
                block[index] = false;
                cnt--;
            }
        }
    }

    void testBranch(int branchNum)
    {
        for (int i = 0; i < branchNum; i++)
        {
            try
            {
                addBranch();
                //延迟槽
                createDelaySlot();
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
            cnt++;
        }
    }
}

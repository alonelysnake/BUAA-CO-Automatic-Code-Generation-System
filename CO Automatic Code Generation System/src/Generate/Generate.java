package Generate;

import Instruction.I.*;
import Instruction.I.IBranch.Beq;
import Instruction.I.IBranch.Bne;
import Instruction.I.ICal.Lui;
import Instruction.I.ICal.Ori;
import Instruction.I.ILS.*;
import Instruction.J.J;
import Instruction.J.JInstruction;
import Instruction.J.Jal;
import Instruction.R.*;
import Exception.UnknownInstrException;

import java.util.*;

public class Generate
{
    private int instructionNum;//指令条数
    private int cnt = 0;//当前指令index
    final private int labelBlockSize = 3;//label块大小
    private Set<String> R;//选择的R指令
    private Set<String> I;//选择的I指令（不分支）
    private Set<String> JR;//jalr,jr
    private Set<String> LS;//load/save指令
    private Set<String> J;//实现jump的指令（j，jal，jalr，jr）
    private Set<String> branch;//I中的分支指令

    final private int SEED = 114514;//神奇的随机数种子
    Random random = new Random(SEED);

    RInstruction rInstruction;
    IInstruction iInstruction;
    JInstruction jInstruction;

    HashSet<Integer> writeProhibit = new HashSet<>();//不可以做运算写入的寄存器（系统用和jr/jalr用的）
    HashSet<Integer> hasVal = new HashSet<>();//有值的寄存器
    List<Integer> addrList = new ArrayList<>();//有值的存储器地址
    List<String> labelList = new ArrayList<>();//存储分支指令的出现顺序

    List<String> ans = new ArrayList<>();//输出的字符串数组

    public Generate(int instructionNum, Set<String> R, Set<String> I, Set<String> branch, Set<String> J)
    {
        this.setInstructionNum(instructionNum);
        this.setR(R);
        this.setI(I);
        this.setBranch(branch);
        this.setJ(J);
        this.initWriteProhibit();
    }

    private void initWriteProhibit()
    {
        writeProhibit.add(0);
        writeProhibit.add(1);
        writeProhibit.add(2);
        writeProhibit.add(3);
        writeProhibit.add(26);
        writeProhibit.add(27);
        writeProhibit.add(28);
        writeProhibit.add(29);
        writeProhibit.add(30);
        writeProhibit.add(31);
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
            iInstruction = new Ori(writeProhibit, this.hasVal);
            this.ans.add(iInstruction.createMIPSText());
            //增加有值的寄存器
            hasVal.add(iInstruction.getRt());
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
                if (this.R.contains("jalr") && jalr)
                {
                    //未实现
                    System.out.println("未实现");
                } else
                {
                    //可以考虑ori给其他寄存器然后jr那个
                    //ori
                    iInstruction = new Ori(this.writeProhibit, 31, "0");
                    ans.add(iInstruction.createMIPSText());
                    //jr
                    rInstruction = new Jr(iInstruction.getRt(), this.writeProhibit);
                    ans.add(rInstruction.createMIPSText());
                }
            } else if (instr.equals("jalr"))
            {
                System.out.println("未实现");
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
                            iInstruction = new Beq(label);
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
                addDelaySlot();
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
                    addR();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            } else
            {
                //I指令
                try
                {
                    addI();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            }
        }
    }

    public int getInstructionNum()
    {
        return instructionNum;
    }

    public Set<String> getR()
    {
        return R;
    }

    public Set<String> getI()
    {
        return I;
    }

    public Set<String> getJ()
    {
        return J;
    }

    public void setInstructionNum(int instructionNum)
    {
        this.instructionNum = instructionNum;
    }

    public void setR(Set<String> r)
    {
        R = r;
    }

    public void setI(Set<String> i)
    {
        I = i;
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
        int RNum = (instructionNum - 6) / 3;//R指令数目
        int Jnum = 2;//J型（jal）指令数目
        int INum = instructionNum - 6 - RNum - Jnum;//I指令数目
        int BranchNum = 5;

        //以上是寄存器与存储器的初始化部分
        this.initReg();

        //R指令
        for (int i = 0; i < RNum; i++)
        {
            try
            {
                addR();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }

        //J指令（jal）
        for (int i = 0; i < Jnum; i++)
        {
            jInstruction = new Jal(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("jal");
            this.ans.add(("nop"));//延迟槽
        }

        //I指令（无跳转的）
        for (int i = 0; i < INum; i++)
        {
            try
            {
                addI();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }

        //I指令（分支跳转的）
        for (int i = 0; i < 2; i++)
        {
            try
            {
                addBranch();
                //延迟槽
                addDelaySlot();
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
            cnt++;
        }

        if (this.J.contains("j"))
        {
            this.jInstruction = new J(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("j");
            //延迟槽
            addDelaySlot();
        }

        //至此所有不涉及跳转分支的都应该运行完了，会通过end标签直接指向程序终止（通过）
        this.ans.add("beq $0, $0, end");
        //延迟槽
        addDelaySlot();

        //创建最后的label部分
        this.createLabel();

        //保证程序运行结束
        this.ans.add("end:");
        FileGenerate fileGenerate = new FileGenerate(this.ans);
        fileGenerate.update();
    }

    private void addR() throws UnknownInstrException
    {
        int index;
        List<String> RList = new ArrayList<>(R);

        index = this.random.nextInt(RList.size());
        String instruction = RList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "addu":
                this.rInstruction = new Addu(this.writeProhibit, this.hasVal);
                break;
            case "subu":
                this.rInstruction = new Subu(this.writeProhibit, this.hasVal);
                break;
            case "or":
                this.rInstruction = new Or(this.writeProhibit, this.hasVal);
                break;
            case "sll":
                this.rInstruction = new Sll(this.writeProhibit, this.hasVal);
                break;
            case "sllv":
                this.rInstruction = new Sllv(this.writeProhibit, this.hasVal);
                break;
            case "slt":
                this.rInstruction = new Slt(this.writeProhibit, this.hasVal);
                break;
            case "jr":
            case "jalr":
                return;
            default:
                throw new UnknownInstrException(instruction);
        }

        newInstr = this.rInstruction.createMIPSText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("添加 " + instruction + " 指令成功");
    }

    //增加非分支的I指令
    private void addI() throws UnknownInstrException
    {
        int index;
        List<String> IList = new ArrayList<>(I);

        index = this.random.nextInt(IList.size());
        String instruction = IList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "lui":
                this.iInstruction = new Lui(this.writeProhibit, this.hasVal);
                break;
            case "ori":
                this.iInstruction = new Ori(this.writeProhibit, this.hasVal);
                break;
            case "lw":
                this.iInstruction = new Lw(this.addrList, this.writeProhibit, this.hasVal);
                break;
            case "lh":
                this.iInstruction = new Lh(this.addrList, this.writeProhibit, this.hasVal);
                break;
            case "lb":
                this.iInstruction = new Lb(this.addrList, this.writeProhibit, this.hasVal);
                break;
            case "sw":
                this.iInstruction = new Sw(this.hasVal, this.addrList);
                break;
            case "sh":
                this.iInstruction = new Sh(this.hasVal, this.addrList);
                break;
            case "sb":
                this.iInstruction = new Sb(this.hasVal, this.addrList);
                break;
            case "???":
                return;
            default:
                throw new UnknownInstrException(instruction);
        }

        newInstr = iInstruction.createMIPSText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("添加 " + instruction + " 指令成功");
    }

    //随机增加I指令中产生分支的指令
    private void addBranch() throws UnknownInstrException
    {
        int index;
        List<String> branchList = new ArrayList<>(this.branch);

        index = this.random.nextInt(branchList.size());
        String instruction = branchList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "beq":
                this.iInstruction = new Beq(this.labelList);
                break;
            case "bne":
                this.iInstruction = new Bne(this.labelList);
                break;
            default:
                System.out.println("未知指令" + instruction);
                throw new UnknownInstrException(instruction);
        }

        newInstr = iInstruction.createMIPSText();
        if (newInstr != null)
        {
            ans.add(newInstr);
            this.labelList.add(instruction);
        }
    }

    //添加延迟槽
    private void addDelaySlot()
    {
        boolean add = false;
        while (!add)
        {
            try
            {
                addR();
                add = true;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }
}

import Instruction.I.*;
import Instruction.J.J;
import Instruction.J.JInstruction;
import Instruction.J.Jal;
import Instruction.R.Addu;
import Instruction.R.Jr;
import Instruction.R.RInstruction;
import Instruction.R.Subu;

import java.io.*;
import java.util.*;

public class Generate
{
    private int instructionNum;//指令条数
    private int cnt = 0;//当前指令index
    final private int labelBlockSize = 3;//label块大小
    private Set<String> R;//选择的R指令
    private Set<String> I;//选择的I指令（不分支）
    private Set<String> J;//选择的J指令
    private Set<String> branch;//I中的分支指令

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
        this.ans.add("ori $v0, $0, 0xffff");
        //$v1($3)值固定为-2^32
        this.ans.add("lui $v1, 0xffff");
        this.ans.add("ori $v1, $0, 0xffff");
        //随机几个寄存器赋值
        for (int i = 0; i < 3; i++)
        {
            iInstruction = new Ori(writeProhibit);
            this.ans.add(iInstruction.createMIPSText());
            //增加有值的寄存器
            hasVal.add(iInstruction.getRt());
            this.cnt++;
        }
        //存储部分值（方便后续取指令测试）
        for (int i = 0; i < 3; i++)
        {
            this.iInstruction = new Sw(this.hasVal);
            this.ans.add(iInstruction.createMIPSText());
            this.addrList.add(Integer.valueOf(iInstruction.getImm16()));
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
                Random random = new Random();
                boolean jalr = random.nextBoolean();
                if (this.R.contains("jalr") && jalr)
                {
                    //未实现
                    System.out.println("未实现");
                } else
                {
                    //可以考虑ori给其他寄存器然后jr那个
                    //ori
                    iInstruction = new Ori(this.writeProhibit, 31);
                    ans.add(iInstruction.createMIPSText());
                    //jr
                    rInstruction = new Jr(iInstruction.getRt());
                    ans.add(rInstruction.createMIPSText());
                }
            } else if (instr.equals("jalr"))
            {
                System.out.println("未实现");
            } else if (this.branch.contains(instr))
            {
                //beq，bne，bgez这种
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
                ans.add("j end");
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
            boolean add = false;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            int mode = random.nextInt(2);
            int index;
            if (mode == 0)
            {
                //R指令
                List<String> RList = new ArrayList<>(R);
                while (!add)
                {
                    index = random.nextInt(RList.size());
                    add = addR(index, RList);
                }
            } else
            {
                //I指令
                List<String> Ilist = new ArrayList<>(I);
                while (!add)
                {
                    index = random.nextInt(Ilist.size());
                    add = addI(index, Ilist);
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
            boolean add = false;
            int index;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            List<String> RList = new ArrayList<>(R);

            while (!add)
            {
                index = random.nextInt(RList.size());
                add = addR(index, RList);
            }

            cnt++;
        }

        //J指令（jal）
        for (int i = 0; i < Jnum; i++)
        {
            jInstruction = new Jal(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("jal");
        }

        //I指令（无跳转的）
        for (int i = 0; i < INum; i++)
        {
            boolean add = false;
            int index;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            List<String> IList = new ArrayList<>(I);

            while (!add)
            {
                index = random.nextInt(IList.size());
                add = addI(index, IList);
            }

            cnt++;
        }

        //I指令（分支跳转的）
        for (int i = 0; i < BranchNum; i++)
        {
            boolean add = false;
            int index;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            List<String> branchList = new ArrayList<>(this.branch);

            while (!add)
            {
                index = random.nextInt(branchList.size());
                add = addBranch(index, branchList);
            }

            cnt++;
        }

        if (this.J.contains("j"))
        {
            this.jInstruction = new J(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("j");
        }

        //至此所有不涉及跳转分支的都应该运行完了，会通过end标签直接指向程序终止（通过）
        this.ans.add("beq $0, $0, end");

        //创建最后的label部分
        this.createLabel();

        //保证程序运行结束
        this.ans.add("end:");
        this.update(this.ans);
    }

    public boolean addR(int index, List<String> RList)
    {
        String instruction = RList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "addu":
                this.rInstruction = new Addu(this.writeProhibit, this.hasVal);
                newInstr = this.rInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(rInstruction.getRd());
                break;
            case "subu":
                this.rInstruction = new Subu(this.writeProhibit, this.hasVal);
                newInstr = this.rInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(rInstruction.getRd());
                break;
            case "jr":
            case "jalr":
                return false;
            default:
                System.out.println("未知指令" + instruction);
                return false;
        }
        //System.out.println("添加 " + instruction + " 指令成功");
        return true;
    }

    //增加非分支的I指令
    public boolean addI(int index, List<String> IList)
    {
        String instruction = IList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "lui":
                this.iInstruction = new Lui(this.writeProhibit);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(iInstruction.getRt());
                break;
            case "ori":
                this.iInstruction = new Ori(this.writeProhibit);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(iInstruction.getRt());
                break;
            case "lw":
                this.iInstruction = new Lw(this.addrList, this.writeProhibit);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(iInstruction.getRt());
                break;
            case "sw":
                this.iInstruction = new Sw(this.hasVal);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.addrList.add(Integer.valueOf(iInstruction.getImm16()));
                break;
            default:
                System.out.println("未知指令" + instruction);
                return false;
        }
        //System.out.println("添加 " + instruction + " 指令成功");
        return true;
    }

    //随机增加I指令中产生分支的指令
    public boolean addBranch(int index, List<String> branchList)
    {
        String instruction = branchList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "beq":
                this.iInstruction = new Beq(instructionNum * 4, cnt * 4, this.labelList);
                break;
            case "bne":
                break;
            default:
                System.out.println("未知指令" + instruction);
                return false;
        }

        newInstr = iInstruction.createMIPSText();
        if (newInstr == null)
        {
            return false;
        }
        ans.add(newInstr);
        this.labelList.add(instruction);

        return true;
    }

    public void update(List<String> ans)
    {
        String fileName = "output.txt";
        try
        {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileName)));
            for (String text : ans)
            {
                bw.write(text);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e)
        {
            System.out.println("写入失败了。。。");
        }
    }
}

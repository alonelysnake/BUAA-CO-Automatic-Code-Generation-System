import Instruction.I.*;
import Instruction.J.JInstruction;
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
    private Set<String> R;//选择的R指令
    private Set<String> I;//选择的I指令
    private Set<String> J;//选择的J指令

    RInstruction rInstruction;
    IInstruction iInstruction;
    JInstruction jInstruction;

    HashSet<Integer> writeProhibit = new HashSet<>();//不可以做运算写入的寄存器（系统用和jr/jalr用的）
    HashSet<Integer> hasVal = new HashSet<>();//有值的寄存器
    List<Integer> addrList = new ArrayList<>();
    List<String> labelList = new ArrayList<>();

    List<String> ans = new ArrayList<>();//输出的字符串数组

    public Generate(int instructionNum, Set<String> R, Set<String> I, Set<String> J)
    {
        this.setInstructionNum(instructionNum);
        this.setR(R);
        this.setI(I);
        this.setJ(J);
        this.initWriteProhibit();
    }

    private void initWriteProhibit()
    {
        writeProhibit.add(0);
        writeProhibit.add(1);
        writeProhibit.add(26);
        writeProhibit.add(27);
        writeProhibit.add(28);
        writeProhibit.add(29);
        writeProhibit.add(30);
        writeProhibit.add(31);
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

    public void setJ(Set<String> j)
    {
        J = j;
    }

    public void Run()
    {
        int RNum = (instructionNum - 6) / 3;//R指令数目
        int INum = instructionNum - RNum - 6;//I指令数目

        for (int i = 0; i < 3; i++)
        {
            iInstruction = new Ori(writeProhibit);
            ans.add(iInstruction.createMIPSText());
            //增加有值的寄存器
            hasVal.add(iInstruction.getRt());

            cnt++;
        }
        for (int i = 0; i < 3; i++)
        {
            this.iInstruction = new Sw(this.hasVal);
            ans.add(iInstruction.createMIPSText());
            this.addrList.add(Integer.valueOf(iInstruction.getImm16()));

            cnt++;
        }
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

        //至此所有不涉及跳转分支的都应该运行完了，会通过end标签直接指向程序终止（通过）
        ans.add("beq $0, $0, end");
        //以下完成跳转指令，保证每条这样的指令都只会占两条，比如beq和j会是ori+beq，jal是ori+jr
        //保证beq运行完直接到end，jal会对应jr
        for(int i=0;i<this.labelList.size();i++)
        {
            //先输入label 'i' :
            String text="label"+i+":";
            ans.add(text);
            //不同分支跳转指令不同处理
            String instr = this.labelList.get(i);
            switch (instr)
            {
                case "beq":
                    iInstruction=new Ori(this.writeProhibit);
                    ans.add(iInstruction.createMIPSText());
                    ans.add("beq $0, $0, end");
                    break;
                case "j":
                    break;
                case "jal":
                    break;
                case "jalr":
                    break;
                default:
                    System.out.println(" Σ( ° △ °|||)︴  又在分支跳转里放奇怪的指令了");
                    System.out.println("错误指令："+instr);
                    ans.add("beq $0, $0, end");
                    break;
            }
        }

        //保证程序运行结束
        ans.add("end:");
        update(ans);
    }

    public boolean addR(int index, List<String> RList)
    {
        String instruction = RList.get(index);
        switch (instruction)
        {
            case "addu":
                this.rInstruction = new Addu(this.writeProhibit, this.hasVal);
                ans.add(this.rInstruction.createMIPSText());
                break;
            case "subu":
                this.rInstruction = new Subu(this.writeProhibit, this.hasVal);
                ans.add(this.rInstruction.createMIPSText());
                this.hasVal.add(this.rInstruction.getRd());
                break;
            case "jr":
                this.rInstruction = new Jr();
                ans.add(this.rInstruction.createMIPSText());
                break;
            case "jalr":
                break;
            default:
                System.out.println("未知指令" + instruction);
                return false;
        }
        //System.out.println("添加 " + instruction + " 指令成功");
        return true;
    }

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
            case "beq":
                this.iInstruction = new Beq(instructionNum * 4, cnt * 4, this.labelList);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.labelList.add("beq");
                break;
            default:
                System.out.println("未知指令" + instruction);
                return false;
        }
        //System.out.println("添加 " + instruction + " 指令成功");
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

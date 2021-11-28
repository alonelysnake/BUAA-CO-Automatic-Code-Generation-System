package Generate;

import Instruction.InstructionDic;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Init
{
    Set<String> RCal = new HashSet<>();//选择的R指令
    Set<String> ICal = new HashSet<>();//选择的I指令（不分支）
    Set<String> RJ = new HashSet<>();//jalr,jr
    Set<String> LS = new HashSet<>();//load/save指令
    Set<String> J = new HashSet<>();//实现jump的指令（j，jal）
    Set<String> branch = new HashSet<>();//I中的分支指令
    Set<String> MD = new HashSet<>();//乘除法相关

    Set<Integer> writeProhibit;

    Scanner scanner = new Scanner(System.in);

    public Init()
    {
        RCal.add("addu");
        RCal.add("subu");
        ICal.add("ori");
        ICal.add("lui");
        LS.add("sw");
        LS.add("lw");
        branch.add("beq");
        System.out.println("是否要自定义测试指令？（y/n）：");
        String def = scanner.next();
        if (def.equals("y"))
        {
            this.chooseInstr();
        } else
        {
            //默认
            initRCal();
            initICal();
            initBranch();
            initLS();
            initJ();
            initRJ();
            initMD();
        }
        initWriteProhibit();
    }

    public void chooseInstr()
    {
        //自定义
        System.out.println("输入要测试的指令（基础的7条指令为必测指令不用输入，输入end停止）：");
        String instr = scanner.next();
        while (!instr.equals("end"))
        {
            if (InstructionDic.RCALMODE.contains(instr))
            {
                this.RCal.add(instr);
                System.out.println("指令" + instr + "添加成功");
            } else if (InstructionDic.ICALMODE.contains(instr))
            {
                this.ICal.add(instr);
                System.out.println("指令" + instr + "添加成功");
            } else if (InstructionDic.JMODE.contains(instr))
            {
                this.J.add(instr);
                System.out.println("指令" + instr + "添加成功");
            } else if (InstructionDic.BRANCH.contains(instr))
            {
                this.branch.add(instr);
                System.out.println("指令" + instr + "添加成功");
            } else if (InstructionDic.LSMODE.contains(instr))
            {
                this.LS.add(instr);
                System.out.println("指令" + instr + "添加成功");
            } else if (InstructionDic.RJMODE.contains(instr))
            {
                this.RJ.add(instr);
                System.out.println("指令" + instr + "添加成功");
            } else if (InstructionDic.MDMODE.contains(instr))
            {
                this.MD.add(instr);
                System.out.println("指令" + instr + "添加成功");
            } else
            {
                System.out.println(instr + "为无效（未实现）指令，添加失败");
            }
            System.out.print("继续输入要添加的指令：");
            instr = scanner.next();
        }
    }

    public void initRCal()
    {
        this.RCal = new HashSet<>();
        RCal.add("and");
        RCal.add("nor");
        RCal.add("or");
        RCal.add("xor");
        RCal.add("sra");
        RCal.add("srav");
        RCal.add("srl");
        RCal.add("srlv");
        RCal.add("sll");
        RCal.add("sllv");
        RCal.add("slt");
    }

    public void initICal()
    {
        this.ICal = new HashSet<>();
        ICal.add("addi");
        ICal.add("andi");
        ICal.add("slti");
        ICal.add("xori");
    }

    public void initBranch()
    {
        this.branch = new HashSet<>();
        branch.add("bne");
        branch.add("bgez");
        branch.add("blez");
        branch.add("bgtz");
        branch.add("bltz");
    }

    public void initLS()
    {
        this.LS = new HashSet<>();
        LS.add("lh");
        LS.add("lb");
        LS.add("sh");
        LS.add("sb");
        ;
    }

    public void initRJ()
    {
        this.RJ = new HashSet<>();
        RJ.add("jr");
        RJ.add("jalr");
    }

    public void initJ()
    {
        this.J = new HashSet<>();
        J.add("j");
        J.add("jal");
    }

    public void initMD()
    {
        this.MD = new HashSet<>();
        MD.add("mult");
        MD.add("div");
        MD.add("mfhi");
        MD.add("mflo");
        MD.add("mthi");
        MD.add("mtlo");
    }

    public void initWriteProhibit()
    {
        this.writeProhibit = new HashSet<>();
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

}

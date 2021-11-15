import Instruction.InstructionDic;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class UI
{
    Set<String> R;
    Set<String> I;
    Set<String> branch;
    Set<String> J;
    int num;

    private Generate generate;

    public UI()
    {
        this.R = new HashSet<>();
        this.I = new HashSet<>();
        this.branch = new HashSet<>();
        this.J = new HashSet<>();
    }

    public void showInstruction()
    {
        System.out.println("目前支持指令：");
        System.out.println("R:addu    subu    jr");
        System.out.println("I:ori    lui    sw    lw    beq");
        System.out.println("J:j(p4不要求所以暂时禁用)    jal");
    }

    public void chooseInstruction()
    {
        this.showInstruction();
        //必测指令
        R.add("addu");
        R.add("subu");
        I.add("ori");
        I.add("lui");
        I.add("sw");
        I.add("lw");
        branch.add("beq");

        Scanner scanner = new Scanner(System.in);
        System.out.println("是否要自定义测试指令？（y/n）：");
        String def = scanner.next();
        if (def.equals("y"))
        {
            //自定义
            System.out.println("输入要测试的指令（基础的7条指令为必测指令不用输入，输入end停止）：");
            String instr = scanner.next();
            while (!instr.equals("end"))
            {
                if (InstructionDic.RMODE.contains(instr))
                {
                    this.R.add(instr);
                    System.out.println("指令" + instr + "添加成功");
                } else if (InstructionDic.IMODE.contains(instr))
                {
                    this.I.add(instr);
                    System.out.println("指令" + instr + "添加成功");
                } else if (InstructionDic.JMODE.contains(instr))
                {
                    this.J.add(instr);
                    System.out.println("指令" + instr + "添加成功");
                } else if (InstructionDic.BRANCH.contains(instr))
                {
                    this.branch.add(instr);
                    System.out.println("指令" + instr + "添加成功");
                } else
                {
                    System.out.println(instr + "为无效（未实现）指令，添加失败");
                }
                System.out.print("继续输入要添加的指令：");
                instr = scanner.next();
            }
        } else
        {
            //默认
            //J.add("j");
            J.add("jal");
        }

        System.out.print("要生成的指令总数为(建议在20-30条)：");
        this.num = scanner.nextInt();
    }

    public void run()
    {
        this.chooseInstruction();
        Generate generate = new Generate(num, R, I, branch, J);
        generate.run();
    }
}

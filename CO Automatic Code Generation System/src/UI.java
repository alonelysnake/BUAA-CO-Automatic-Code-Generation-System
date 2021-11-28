import Generate.Generate;
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
        System.out.println("J:j    jal");
    }

    public void run()
    {
        this.showInstruction();
        Generate generate = new Generate();
        generate.run();
    }
}

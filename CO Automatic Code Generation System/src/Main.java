import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main
{
    //程序入口，运行过程：
    //一系列R指令和赋值I指令
    //一系列存取数指令
    //一系列beq指令+赋值存取数指令
    //一系列j+jal
    public static void main(String[] args)
    {
        Set<String> R = new HashSet<>();
        Set<String> I = new HashSet<>();
        Set<String> J = new HashSet<>();

        R.add("addu");
        R.add("subu");
        I.add("ori");
        I.add("lui");
        I.add("sw");
        I.add("lw");
        I.add("beq");
        //J.add("j");
        J.add("jal");

        System.out.println("目前支持指令：");
        System.out.println("R:addu    subu    jr");
        System.out.println("I:ori    lui    sw    lw    beq");
        System.out.println("J:j    jal");
        System.out.print("要生成的指令总数为(建议在20-30条)：");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        Generate generate = new Generate(num, R, I, J);
        generate.Run();
        System.out.println("指令生成完成");
        try
        {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

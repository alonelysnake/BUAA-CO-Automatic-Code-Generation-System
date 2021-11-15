import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{
    //程序入口，运行过程：
    //一系列R指令和赋值I指令
    //一系列存取数指令
    //一系列j+jal
    // 一系列beq指令+赋值存取数指令

    public static void main(String[] args)
    {
        UI ui = new UI();
        ui.run();
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

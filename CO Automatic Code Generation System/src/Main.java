import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main
{
    //������ڣ����й��̣�
    //һϵ��Rָ��͸�ֵIָ��
    //һϵ�д�ȡ��ָ��
    //һϵ��beqָ��+��ֵ��ȡ��ָ��
    //һϵ��j+jal
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

        System.out.println("Ŀǰ֧��ָ�");
        System.out.println("R:addu    subu    jr");
        System.out.println("I:ori    lui    sw    lw    beq");
        System.out.println("J:j    jal");
        System.out.print("Ҫ���ɵ�ָ������Ϊ(������20-30��)��");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        Generate generate = new Generate(num, R, I, J);
        generate.Run();
        System.out.println("ָ���������");
        try
        {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

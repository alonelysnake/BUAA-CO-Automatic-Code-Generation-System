import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{
    //������ڣ����й��̣�
    //һϵ��Rָ��͸�ֵIָ��
    //һϵ�д�ȡ��ָ��
    //һϵ��j+jal
    // һϵ��beqָ��+��ֵ��ȡ��ָ��

    public static void main(String[] args)
    {
        UI ui = new UI();
        ui.run();
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

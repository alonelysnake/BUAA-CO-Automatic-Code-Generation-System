package Interface;

import java.util.Random;
import java.util.Set;

//���ڿ�д��Ĵ����У�������ȫ����ļĴ������
public interface RandomReg
{
    public default int randomWriteReg(Set<Integer> writeProhibit)
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rd;
        int cnt = 0;//���Ĵ����Ƿ���޸�
        while (cnt < 100)
        {
            rd = random.nextInt(28);
            //����ֵ�ļĴ��������ǿ��޸ĵ�
            if (!writeProhibit.contains(rd))
            {
                return rd;
            }
            cnt++;
        }
        System.out.println("���мĴ�����������ʹ��");
        System.exit(0);
        return 0;
    }

    public default int randomReadReg()
    {
        System.out.println("randomReadReg��δʵ��");
        return 0;
    }

    public default int conflictReadReg()
    {
        System.out.println("conflictReadReg��δʵ��");
        return 0;
    }
}

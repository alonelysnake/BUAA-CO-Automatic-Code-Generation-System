package Instruction.R;

import java.util.Random;

abstract public class RCalInstruction extends RInstruction
{
    //Ĭ�Ͽ�д�����
    @Override
    protected int chooseRd()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rd;
        int cnt = 0;//���Ĵ����Ƿ���޸�
        while (cnt < 100)
        {
            rd = random.nextInt(28);
            //����ֵ�ļĴ��������ǿ��޸ĵ�
            if (!this.getWriteProhibit().contains(rd))
            {
                return rd;
            }
            cnt++;
        }
        System.out.println("���мĴ�����������ʹ��");
        System.exit(0);
        return 0;
    }

    //Ĭ�����
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rs;
        int cnt = 0;//���Ĵ����Ƿ���ֵ
        while (cnt < 100)
        {
            rs = random.nextInt(28);
            //����ֵ�ļĴ�����������ֵ��
            if (this.getHasVal().contains(rs))
            {
                return rs;
            }
            cnt++;
        }
        System.out.println("���Ѹ�ֵ�Ĵ���");
        System.exit(0);
        return 0;
    }

    //Ĭ�����
    @Override
    protected int chooseRt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rt;
        int cnt = 0;//���Ĵ����Ƿ���ֵ
        while (cnt < 100)
        {
            rt = random.nextInt(28);
            //����ֵ�ļĴ�����������ֵ��
            if (this.getHasVal().contains(rt))
            {
                return rt;
            }
            cnt++;
        }
        System.out.println("���Ѹ�ֵ�Ĵ���");
        System.exit(0);
        return 0;
    }

    //Ĭ��Ϊ0
    @Override
    protected int chooseShamt()
    {
        return 0;
    }
}

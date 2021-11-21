package Instruction.I.ICal;

import Instruction.I.IInstruction;

import java.util.Random;

abstract public class ICalInstruction extends IInstruction
{
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

    @Override
    protected int chooseRt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rt;
        int cnt = 0;//���Ĵ����Ƿ���޸�
        while (cnt < 100)
        {
            rt = random.nextInt(28);
            //����ֵ�ļĴ��������ǿ��޸ĵ�
            if (!this.getWriteProhibit().contains(rt))
            {
                return rt;
            }
            cnt++;
        }
        System.out.println("���мĴ�����������ʹ��");
        System.exit(0);
        return 0;
    }

    @Override
    protected int chooseImm16()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(65535);
    }

    @Override
    protected String chooseLabel()
    {
        return null;
    }

    @Override
    protected void setValue()
    {
        this.setRs(this.chooseRs());
        this.setRt(this.chooseRt());
        this.setImm16(this.chooseImm16());
        this.setText(this.createMIPSText());
    }
}

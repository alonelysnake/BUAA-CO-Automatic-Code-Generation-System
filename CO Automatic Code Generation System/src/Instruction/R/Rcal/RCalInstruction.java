package Instruction.R.Rcal;

import Instruction.R.RInstruction;
import Interface.RandomReg;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

abstract public class RCalInstruction extends RInstruction implements RandomReg
{
    public RCalInstruction()
    {
    }

    //�������
    public RCalInstruction(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setConflictReg(conflictReg);

        this.setValue();

        if (this.getRd() != 0)
        {
            hasVal.add(this.getRd());
            conflictReg.addLast(this.getRd());
            if (conflictReg.size() > 3)
            {
                conflictReg.removeFirst();
            }
        }
    }

    //Ĭ�Ͽ�д�����
    @Override
    protected int chooseRd()
    {
        return randomWriteReg(this.getWriteProhibit());
    }

    //Ĭ�����
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int size = this.getConflictReg().size();
        if (size < 3 || random.nextInt(10) < 2)
        {
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
        int index = random.nextInt(size);
        return this.getConflictReg().get(index);
    }

    //�����������ֵ����������ת����������
/*
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
*/
    //Ĭ�����
    @Override
    protected int chooseRt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int size = this.getConflictReg().size();
        if (size < 3 || random.nextInt(10) < 2)
        {
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
        int index = random.nextInt(size);
        //�ٴ����������ȸ���
        if (this.getConflictReg().get(index) == this.getRs())
        {
            index = random.nextInt(size);
        }
        return this.getConflictReg().get(index);
    }

    //Ĭ��Ϊ0
    @Override
    protected int chooseShamt()
    {
        return 0;
    }
}

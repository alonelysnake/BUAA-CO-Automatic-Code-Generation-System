package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Sllv extends RInstruction
{
    public Sllv(Set<Integer> writeProhibit, Set<Integer> hasVal)
    {
        this.setFunc(InstructionDic.SLLV);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
    }

    @Override
    protected int chooseRd()
    {
        int seed=new Random().nextInt();
        Random random=new Random(seed);
        int rd;
        int cnt=0;//���Ĵ����Ƿ���޸�
        while(cnt<100)
        {
            rd=random.nextInt(28);
            //����ֵ�ļĴ��������ǿ��޸ĵ�
            if(!this.getWriteProhibit().contains(rd))
            {
                return rd;
            }
            cnt++;
        }
        System.out.println("���мĴ�����������ʹ��");
        System.exit(0);
        return 0;
    }

    @Override
    protected int chooseRs()
    {
        int seed=new Random().nextInt();
        Random random=new Random(seed);
        int rs;
        int cnt=0;//���Ĵ����Ƿ���޸�
        while(cnt<100)
        {
            rs=random.nextInt(28);
            //����ֵ�ļĴ�����������ֵ��
            if(this.getHasVal().contains(rs))
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
        return this.chooseRs();
    }

    @Override
    protected int chooseShamt()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        super.createMIPSText();

        //sllv rd, rs, rt
        return "sllv $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}
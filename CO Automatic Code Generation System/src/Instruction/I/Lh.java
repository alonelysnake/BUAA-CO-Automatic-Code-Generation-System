package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Lh extends IInstruction
{
    private final List<Integer> addrList;//�Ѿ�д��ĵ�ַ

    public Lh(List<Integer> addrList, Set<Integer> writeProhibit)
    {
        this.setOp(InstructionDic.LH);
        this.addrList = addrList;
        this.setWriteProhibit(writeProhibit);
    }

    @Override
    protected int chooseRs()
    {
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
    protected String chooseImm16()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int index = random.nextInt(addrList.size());
        int imm = addrList.get(index);
        imm += random.nextInt(2) * 2;
        return Integer.toString(imm);
    }

    @Override
    protected String chooseLabel()
    {
        return null;
    }

    @Override
    public String createMIPSText()
    {
        super.createMIPSText();
        //lh rt, imm16(rs)  �̶�rsΪ0
        return "lh $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                ", ($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}
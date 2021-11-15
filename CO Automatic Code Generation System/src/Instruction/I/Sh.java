package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Sh extends IInstruction
{
    private final Set<Integer> hasVal;

    public Sh(Set<Integer> hasVal)
    {
        this.setOp(InstructionDic.SH);
        this.hasVal = hasVal;
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
        int cnt = 0;//����Ƿ��б��޸Ĺ��ļĴ���
        while (cnt < 100)
        {
            rt = random.nextInt(28);
            //���и�ֵ�ļĴ�����������ֵ��
            if (this.hasVal.contains(rt))
            {
                return rt;
            }
            cnt++;
        }
        System.out.println("���мĴ�����δ��ֵ");
        System.exit(0);
        return 0;
    }

    @Override
    protected String chooseImm16()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int imm = random.nextInt(100) * 2;
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
        //sh rt, imm16(rs)  �̶��洢��ַΪ50����,rs=0,Ҫ��GPR[rt]!=0
        return "sh $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                "($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}

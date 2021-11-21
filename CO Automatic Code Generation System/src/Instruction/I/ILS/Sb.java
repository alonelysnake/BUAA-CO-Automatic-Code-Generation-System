package Instruction.I.ILS;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Sb extends ILSInstruction
{
    private final Set<Integer> hasVal;

    public Sb(Set<Integer> hasVal, List<Integer> addrList)
    {
        this.setOp(InstructionDic.SB);
        this.hasVal = hasVal;
        this.setAddrList(addrList);
        this.setValue();
        int addr = (this.getImm16() / 4) * 4;
        if (!addrList.contains(addr))
        {
            addrList.add(addr);
        }
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
    protected int chooseImm16()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(200);
    }

    @Override
    protected String chooseLabel()
    {
        return null;
    }

    @Override
    public String createMIPSText()
    {
        //sb rt, imm16(rs)  �̶��洢��ַΪ50����,rs=0,Ҫ��GPR[rt]!=0
        return "sb $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                "($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}

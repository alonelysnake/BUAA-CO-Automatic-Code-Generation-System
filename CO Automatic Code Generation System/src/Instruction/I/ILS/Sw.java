package Instruction.I.ILS;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Sw extends ILSInstruction
{
    private final Set<Integer> hasVal;

    public Sw(Set<Integer> hasVal, List<Integer> addrList)
    {
        this.setOp(InstructionDic.SW);
        this.hasVal = hasVal;
        this.setAddrList(addrList);
        this.setValue();
        if (!this.getAddrList().contains(this.getImm16()))
        {
            this.getAddrList().add(this.getImm16());
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
            if (rt != 0 && this.hasVal.contains(rt))
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
        return random.nextInt(50) * 4;
    }

    @Override
    public String createMIPSText()
    {
        //sw rt, imm16(rs)  �̶��洢��ַΪ50����,rs=0,Ҫ��GPR[rt]!=0
        return "sw $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                "($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}

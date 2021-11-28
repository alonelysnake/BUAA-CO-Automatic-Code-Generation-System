package Instruction.I.ILS;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.lang.System.exit;

public class Sh extends ILSInstruction
{
    private final Set<Integer> hasVal;

    public Sh(Set<Integer> hasVal, List<Integer> addrList)
    {
        this.setOp(InstructionDic.SH);
        this.hasVal = hasVal;
        this.setValue();
        int addr = this.getImm16() - this.getImm16() % 4;
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
            if (rt != 0 && this.hasVal.contains(rt))
            {
                return rt;
            }
            cnt++;
        }
        System.out.println("���мĴ�����δ��ֵ");
        exit(0);
        return 0;
    }

    @Override
    protected int chooseImm16()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(100) * 2;
    }

    @Override
    public String createMIPSText()
    {
        //sh rt, imm16(rs)  �̶��洢��ַΪ50����,rs=0,Ҫ��GPR[rt]!=0
        return "sh $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                "($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}

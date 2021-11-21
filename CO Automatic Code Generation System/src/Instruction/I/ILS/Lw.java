package Instruction.I.ILS;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Lw extends ILSInstruction
{
    public Lw(List<Integer> addrList, Set<Integer> writeProhibit,Set<Integer>hasVal)
    {
        this.setOp(InstructionDic.LW);
        this.setAddrList(addrList);
        this.setWriteProhibit(writeProhibit);
        this.setValue();
        hasVal.add(this.getRt());
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
        int index = random.nextInt(this.getAddrList().size());
        return this.getAddrList().get(index);
    }

    @Override
    public String createMIPSText()
    {
        //lw rt, imm16(rs)  �̶�rsΪ0
        return "lw $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                ", ($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}

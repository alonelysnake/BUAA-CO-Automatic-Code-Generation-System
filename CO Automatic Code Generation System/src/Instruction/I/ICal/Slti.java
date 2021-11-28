package Instruction.I.ICal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Slti extends ICalInstruction
{
    public Slti(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setOp(InstructionDic.SLTI);
    }

    //���ǲ���һ����ȡ�һ��С��/���ڵ��������$0,$2,$3�Ĵ�����
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int sel = random.nextInt(4);
        switch (sel)
        {
            case 0:
                //���
                return 0;
            case 1:
                //���
                return 2;
            case 2:
                //��С
                return 3;
            default:
                //���
                return random.nextInt(28);
        }
    }

    //�������ʱ��0�Ĵ���������
    @Override
    protected int chooseImm16()
    {
        if (this.getRs() == 0)
        {
            return 0;
        } else
        {
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            return random.nextInt(32767);
        }
    }

    @Override
    public String createMIPSText()
    {
        //slti rt, rs, imm16 ��16���Ʊ�ʾ
        return "slti $" + RegDic.RegName.get(this.getRt()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", 0x" +
                Integer.toHexString(this.getImm16());
    }
}

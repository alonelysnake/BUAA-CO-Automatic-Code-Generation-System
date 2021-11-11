package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;

public class Beq extends IInstruction
{
    private int range;//beq����ת���ܵ�ַ
    private int nowAddr;//��ǰ��ַ

    public Beq(int range, int nowAddr)
    {
        this.setOp(InstructionDic.BEQ);
        this.range = range;
        this.nowAddr = nowAddr;
    }

    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);

        return random.nextInt(28);
    }

    @Override
    protected int chooseRt()
    {
        Random random = new Random();
        if (random.nextBoolean())
        {
            return this.chooseRs();
        } else
        {
            return this.getRs();
        }
    }

    @Override
    protected String chooseImm16()
    {
        if (this.getRt() == this.getRs())
        {
            return Integer.toString(this.range - this.nowAddr - 4);
        }//���
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int imm = random.nextInt((range-nowAddr-4)*4)/4;
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
        //beq rs, rt, label
        return "beq $" + RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16();
    }
}

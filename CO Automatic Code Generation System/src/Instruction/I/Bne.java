package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;

public class Bne extends IInstruction
{
    final private int range;//beq����ת���ܵ�ַ
    final private int nowAddr;//��ǰ��ַ
    final private List<String> labelList;

    public Bne(int range, int nowAddr, List<String> labelList)
    {
        this.setOp(InstructionDic.BNE);
        this.range = range;
        this.nowAddr = nowAddr;
        this.labelList = labelList;
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
            return this.getRs();
        } else
        {
            return this.chooseRs();
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
        int imm = random.nextInt((range - nowAddr - 4) * 4) / 4;
        return Integer.toString(imm);
    }

    @Override
    protected String chooseLabel()
    {
        return "label" + this.labelList.size() * 2;
    }

    @Override
    public String createMIPSText()
    {
        super.createMIPSText();
        //bne rs, rt, label
        return "bne $" + RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getLabel();
    }
}

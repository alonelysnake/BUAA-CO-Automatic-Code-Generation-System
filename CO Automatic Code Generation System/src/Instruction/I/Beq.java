package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;

public class Beq extends IInstruction
{
    final private int range;//beq可跳转的总地址
    final private int nowAddr;//当前地址
    final private List<String>labelList;

    public Beq(int range, int nowAddr, List<String>labelList)
    {
        this.setOp(InstructionDic.BEQ);
        this.range = range;
        this.nowAddr = nowAddr;
        this.labelList= labelList;
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
        }//恒等
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int imm = random.nextInt((range-nowAddr-4)*4)/4;
        return Integer.toString(imm);
    }

    @Override
    protected String chooseLabel()
    {
        return "label"+this.labelList.size();
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
                this.getLabel();
    }
}

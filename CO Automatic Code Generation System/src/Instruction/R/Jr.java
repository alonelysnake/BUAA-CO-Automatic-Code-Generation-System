package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;

public class Jr extends RInstruction
{
    //先固定jr返回值为31
    public Jr()
    {
        this.setFunc(InstructionDic.JR);
        this.setRs(31);
    }

    @Override
    protected int chooseRd()
    {
        return 0;
    }

    @Override
    protected int chooseRs()
    {
        return 31;
    }

    @Override
    protected int chooseRt()
    {
        return 0;
    }

    @Override
    protected int chooseShamt()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        //jr rs
        return "jr $" + RegDic.RegName.get(this.getRs());
    }
}

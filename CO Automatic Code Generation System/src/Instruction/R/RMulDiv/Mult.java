package Instruction.R.RMulDiv;

import Instruction.InstructionDic;
import Instruction.RegDic;

public class Mult extends RMulDivInstruction
{
    //需要保证HI LO都可能不是0
    public Mult(int rs, int rt)
    {
        this.setRs(rs);
        this.setRt(rt);
        this.setValue();
        this.setFunc(InstructionDic.MULT);
    }

    @Override
    protected int chooseRs()
    {
        return this.getRs();
    }

    @Override
    protected int chooseRt()
    {
        return this.getRt();
    }

    @Override
    protected int chooseRd()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        //mult rs, rt
        return "mult $" + RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

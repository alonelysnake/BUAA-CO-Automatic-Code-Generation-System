package Instruction.R.RMulDiv;

import Instruction.InstructionDic;
import Instruction.RegDic;

public class Div extends RMulDivInstruction
{
    //需要保证除数（rt）不是0
    public Div(int rs, int rt)
    {
        this.setRs(rs);
        this.setRt(rt);
        this.setValue();
        this.setFunc(InstructionDic.DIV);
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
        //div rs, rt
        return "div $" + RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

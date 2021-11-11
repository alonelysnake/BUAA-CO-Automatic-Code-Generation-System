package Instruction.J;

import Instruction.InstructionDic;

public class J extends JInstruction
{
    public J()
    {
        this.setOp(InstructionDic.J);
    }

    @Override
    protected String chooseImm26()
    {
        return null;
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
        //j label
        return "j " +this.getLabelMap();
    }
}

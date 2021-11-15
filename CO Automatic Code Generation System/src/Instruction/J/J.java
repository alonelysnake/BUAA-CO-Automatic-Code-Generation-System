package Instruction.J;

import Instruction.InstructionDic;

import java.util.List;

public class J extends JInstruction
{
    public J(List<String> labelList)
    {
        this.setOp(InstructionDic.J);
        this.setLabelList(labelList);
    }

    @Override
    protected String chooseImm26()
    {
        return null;
    }

    @Override
    protected String chooseLabel()
    {
        return "label" + this.getLabelList().size() * 2;
    }

    @Override
    public String createMIPSText()
    {
        super.createMIPSText();
        //j label
        return "j " + this.getLabel();
    }
}

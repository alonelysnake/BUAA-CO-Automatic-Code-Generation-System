package Instruction.J;


import Instruction.InstructionDic;

import java.util.List;

public class Jal extends JInstruction
{
    public Jal(int nowAddr, List<String> labelList)
    {
        this.setOp(InstructionDic.JAL);
        this.setLabelList(labelList);
        this.setNowAddr(nowAddr);
        this.setValue();
    }

    public Jal(List<String> labelList)
    {
        this.setOp(InstructionDic.JAL);
        this.setLabelList(labelList);
        this.setValue();
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
        //jal label
        return "jal " + this.getLabel();
    }
}

package Instruction.J;


import Instruction.InstructionDic;

import java.util.HashMap;

public class Jal extends JInstruction
{
    public Jal(int nowAddr, HashMap<String,Integer>labelMap)
    {
        this.setOp(InstructionDic.JAL);
        this.setLabelMap(labelMap);
        this.setNowAddr(nowAddr);
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
        //jal label
        return "jal " + this.getLabelMap();
    }
}

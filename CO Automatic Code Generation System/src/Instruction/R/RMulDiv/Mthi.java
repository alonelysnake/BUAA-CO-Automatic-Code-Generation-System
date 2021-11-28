package Instruction.R.RMulDiv;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Mthi extends RMulDivInstruction
{
    public Mthi(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.MTHI);
    }

    @Override
    protected int chooseRt()
    {
        return 0;
    }

    @Override
    protected int chooseRd()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        //mthi rs
        return "mthi $" + RegDic.RegName.get(this.getRs());
    }
}

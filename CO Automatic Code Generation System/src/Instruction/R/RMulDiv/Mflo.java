package Instruction.R.RMulDiv;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Mflo extends RMulDivInstruction
{
    public Mflo(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.MFLO);
    }

    @Override
    protected int chooseRs()
    {
        return 0;
    }

    @Override
    protected int chooseRt()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        //mflo rd
        return "mflo $" + RegDic.RegName.get(this.getRd());
    }
}

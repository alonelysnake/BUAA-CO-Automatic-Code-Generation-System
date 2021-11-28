package Instruction.R.RMulDiv;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Mfhi extends RMulDivInstruction
{
    public Mfhi(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.MFHI);
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
        //mfhi rd
        return "mfhi $" + RegDic.RegName.get(this.getRd());
    }
}

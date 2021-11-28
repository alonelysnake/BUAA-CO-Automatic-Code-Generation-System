package Instruction.R.RMulDiv;

import Instruction.R.Rcal.RCalInstruction;

import java.util.LinkedList;
import java.util.Set;

abstract public class RMulDivInstruction extends RCalInstruction
{
    public RMulDivInstruction()
    {
    }

    public RMulDivInstruction(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
    }

}

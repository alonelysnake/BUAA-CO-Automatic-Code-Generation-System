package Instruction.R.RMulDiv;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Mtlo extends RMulDivInstruction
{
    public Mtlo(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.MTLO);
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
        //mtlo rs
        return "mtlo $" + RegDic.RegName.get(this.getRs());
    }
}

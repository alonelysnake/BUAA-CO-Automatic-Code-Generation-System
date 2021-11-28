package Instruction.R.Rcal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Slt extends RCalInstruction
{
    public Slt(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.SLT);
    }

    //rs¹Ì¶¨Îª0
    @Override
    protected int chooseRs()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        //slt rd, rs, rt
        return "slt $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

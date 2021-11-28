package Instruction.R.Rcal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Or extends RCalInstruction
{
    public Or(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal,conflictReg);
        this.setFunc(InstructionDic.OR);
    }

    @Override
    public String createMIPSText()
    {
        //or rd, rs, rt
        return "or $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

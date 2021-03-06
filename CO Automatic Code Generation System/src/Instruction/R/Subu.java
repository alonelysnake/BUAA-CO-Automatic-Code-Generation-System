package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Set;

public class Subu extends RCalInstruction
{
    public Subu(Set<Integer> writeProhibit, Set<Integer> hasVal)
    {
        this.setFunc(InstructionDic.SUBU);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setValue();
        hasVal.add(this.getRd());
    }

    @Override
    public String createMIPSText()
    {
        //subu rd, rs, rt
        return "subu $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

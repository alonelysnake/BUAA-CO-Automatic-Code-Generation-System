package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Set;

public class Nor extends  RCalInstruction
{
    public Nor(Set<Integer> writeProhibit, Set<Integer> hasVal)
    {
        this.setFunc(InstructionDic.NOR);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setValue();
        hasVal.add(this.getRd());
    }

    @Override
    public String createMIPSText()
    {
        //nor rd, rs, rt
        return "nor $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

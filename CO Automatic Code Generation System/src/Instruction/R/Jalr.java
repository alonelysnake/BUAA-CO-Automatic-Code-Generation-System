package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Set;

public class Jalr extends RCalInstruction
{
    public Jalr(Set<Integer> writeProhibit, Set<Integer>hasVal, int rs)
    {
        this.setFunc(InstructionDic.JALR);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setValue(rs);
        hasVal.add(this.getRd());
        writeProhibit.add(this.getRd());
    }

    public void setValue(int rs)
    {
        this.setRd(this.chooseRd());
        this.setRs(rs);
        this.setRt(this.chooseRt());
        this.setShamt(this.chooseShamt());
        this.setText(this.createMIPSText());
    }

    //rt¹Ì¶¨Îª0
    @Override
    protected int chooseRt()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        //jalr rd, rs
        return "jalr $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs());
    }
}

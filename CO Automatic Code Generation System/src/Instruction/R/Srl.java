package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Srl extends RCalInstruction
{
    public Srl(Set<Integer> writeProhibit, Set<Integer> hasVal)
    {
        this.setFunc(InstructionDic.SRL);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setValue();
        hasVal.add(this.getRd());
    }

    //rs¹Ì¶¨Îª0
    @Override
    protected int chooseRs()
    {
        return 0;
    }

    @Override
    protected int chooseShamt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(31);
    }

    @Override
    public String createMIPSText()
    {
        //srl rd, rt, shamt
        return "srl $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRt()) +
                ", " +
                getShamt();
    }
}

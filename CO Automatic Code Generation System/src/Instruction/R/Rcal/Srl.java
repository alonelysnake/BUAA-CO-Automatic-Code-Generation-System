package Instruction.R.Rcal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Srl extends RCalInstruction
{
    public Srl(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.SRL);
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
        return random.nextInt(8);
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

package Instruction.R.Rcal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Subu extends RCalInstruction
{
    public Subu(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.SUBU);
    }

    //�ٽ���һ��������Խ��ͳ���rd=0�ĸ���
    @Override
    protected int chooseRt()
    {
        int rt = super.chooseRt();
        if (rt == this.getRs())
        {
            Random random = new Random();
            rt = random.nextInt(28);
        }
        return rt;
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

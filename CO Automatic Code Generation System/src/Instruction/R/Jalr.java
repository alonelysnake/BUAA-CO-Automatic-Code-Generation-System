package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Jalr extends RJumpInstruction
{
    public Jalr(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg, int rs)
    {
        this.setFunc(InstructionDic.JALR);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setRs(rs);

        this.setValue();
        hasVal.add(this.getRd());
        writeProhibit.add(this.getRd());
        conflictReg.addLast(this.getRd());
        if (conflictReg.size() > 3)
        {
            conflictReg.removeFirst();
        }
    }

    @Override
    protected int chooseRd()
    {
        //jalrʹ��1��$at�Ĵ�����
        System.out.println("jalr��chooseRdδ���");
        return 1;
    }

    @Override
    protected int chooseRs()
    {
        return this.getRs();
    }

    //rt�̶�Ϊ0
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

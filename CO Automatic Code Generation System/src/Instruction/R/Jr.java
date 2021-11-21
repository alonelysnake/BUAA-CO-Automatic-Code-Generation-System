package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Set;

public class Jr extends RInstruction
{
    //�ȹ̶�jr����ֵΪ31
    public Jr(int rsNum, Set<Integer> writeProhibit)
    {
        this.setFunc(InstructionDic.JR);
        this.setWriteProhibit(writeProhibit);
        this.setRs(rsNum);
        this.setValue();
        if(rsNum!=31)
        {
            writeProhibit.remove(rsNum);//jr֮��Ϳ���ʹ����
        }
    }

    @Override
    protected int chooseRd()
    {
        return 0;
    }

    @Override
    protected int chooseRs()
    {
        return this.getRs();
    }

    @Override
    protected int chooseRt()
    {
        return 0;
    }

    @Override
    protected int chooseShamt()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        //jr rs
        return "jr $" + RegDic.RegName.get(this.getRs());
    }
}

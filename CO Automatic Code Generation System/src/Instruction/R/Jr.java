package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Set;

public class Jr extends RJumpInstruction
{

    public Jr(int rsNum)
    {
        this.setFunc(InstructionDic.JR);
        this.setRs(rsNum);

        this.setValue();
        /*
        因为有延迟槽的原因，不再认为jr指令执行后马上可用，而是等延迟槽生成完毕后再设置为可用
        if(rsNum!=31)
        {
            writeProhibit.remove(rsNum);//jr之后就可以使用了
        }
        */
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
    public String createMIPSText()
    {
        //jr rs
        return "jr $" + RegDic.RegName.get(this.getRs());
    }
}

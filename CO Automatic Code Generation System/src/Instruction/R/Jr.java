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
        ��Ϊ���ӳٲ۵�ԭ�򣬲�����Ϊjrָ��ִ�к����Ͽ��ã����ǵ��ӳٲ�������Ϻ�������Ϊ����
        if(rsNum!=31)
        {
            writeProhibit.remove(rsNum);//jr֮��Ϳ���ʹ����
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

package Instruction.R.Rcal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Addu extends RCalInstruction
{
    /*
     * 构造方法
     * 传入：
     * 禁止修改的寄存器（运算指令不能用的，包含at这种和存了跳转指令（用于jr）的），默认：0（因为写了也没用），1，26-31
     * 有值的寄存器（用于各种基本运算）（0也可以包含进来）
     * */
    //默认构造
    public Addu(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.ADDU);
    }

    @Override
    public String createMIPSText()
    {
        //addu rd, rs, rt
        return "addu $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

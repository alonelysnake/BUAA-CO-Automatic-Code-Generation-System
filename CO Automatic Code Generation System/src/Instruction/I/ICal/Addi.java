package Instruction.I.ICal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Addi extends ICalInstruction
{
    public Addi(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal,conflictReg);
        this.setOp(InstructionDic.ANDI);
    }

    @Override
    public String createMIPSText()
    {
        //addi rt, rs, imm   用16进制表示
        return "addi $" + RegDic.RegName.get(this.getRt()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", 0x" +
                Integer.toHexString(this.getImm16());
    }
}

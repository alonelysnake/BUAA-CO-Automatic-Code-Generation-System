package Instruction.I.ICal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Set;

public class Andi extends ICalInstruction
{
    public Andi(Set<Integer> writeProhibit, Set<Integer> hasVal)
    {
        this.setOp(InstructionDic.ADDI);
        this.setWriteProhibit(writeProhibit);
        this.setValue();
        hasVal.add(this.getRt());
    }

    @Override
    public String createMIPSText()
    {
        //andi rt, rs, imm   用16进制表示
        return "andi $" + RegDic.RegName.get(this.getRt()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", 0x" +
                Integer.toHexString(this.getImm16());
    }
}

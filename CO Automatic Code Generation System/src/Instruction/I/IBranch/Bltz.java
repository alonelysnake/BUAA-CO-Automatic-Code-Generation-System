package Instruction.I.IBranch;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.List;

public class Bltz extends IBranchInstruction
{
    public Bltz(List<String> labelList, LinkedList<Integer> conflictReg)
    {
        this.setConflictReg(conflictReg);
        this.setOp(InstructionDic.BLTZ);
        this.setLabelList(labelList);
        this.setValue();
        labelList.add("bltz");
    }

    public Bltz(String label)
    {
        this.setOp(InstructionDic.BLTZ);
        this.setValue(label);
    }

    @Override
    protected int chooseRt()
    {
        return 0;
    }

    @Override
    protected void setValue(String label)
    {
        //简化为v1和0（最小值）进行比较，保证触发
        this.setRs(3);
        this.setRt(this.chooseRt());
        this.setLabel(label);
        this.setText(this.createMIPSText());
    }

    @Override
    public String createMIPSText()
    {
        //bltz rs, label
        return "bltz $" + RegDic.RegName.get(this.getRs()) +
                ", " +
                this.getLabel();
    }
}

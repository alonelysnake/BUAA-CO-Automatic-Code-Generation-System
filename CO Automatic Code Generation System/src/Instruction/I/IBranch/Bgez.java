package Instruction.I.IBranch;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Bgez extends IBranchInstruction
{
    public Bgez(List<String> labelList, LinkedList<Integer> conflictReg)
    {
        this.setConflictReg(conflictReg);
        this.setOp(InstructionDic.BGEZ);
        this.setLabelList(labelList);
        this.setValue();
        labelList.add("bgez");
    }

    public Bgez(String label)
    {
        this.setOp(InstructionDic.BGEZ);
        this.setValue(label);
    }

    @Override
    protected int chooseRt()
    {
        return 1;
    }

    @Override
    protected void setValue(String label)
    {
        //简化为v0或0和0（最大值）进行比较，保证触发
        Random random=new Random();
        if(random.nextBoolean())
        {
            this.setRs(2);
        }else {
            this.setRs(0);
        }
        this.setRt(this.chooseRt());
        this.setLabel(label);
        this.setText(this.createMIPSText());
    }

    @Override
    public String createMIPSText()
    {
        //bgez rs, label
        return "bgez $" + RegDic.RegName.get(this.getRs()) +
                ", " +
                this.getLabel();
    }
}

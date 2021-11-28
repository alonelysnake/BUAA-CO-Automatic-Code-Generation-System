package Instruction.I.IBranch;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.List;

public class Bgtz extends IBranchInstruction
{
    public Bgtz(List<String> labelList, LinkedList<Integer> conflictReg)
    {
        this.setConflictReg(conflictReg);
        this.setOp(InstructionDic.BGTZ);
        this.setLabelList(labelList);
        this.setValue();
        labelList.add("bgtz");
    }

    public Bgtz(String label)
    {
        this.setOp(InstructionDic.BGTZ);
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
        //��Ϊv0��0�����ֵ�����бȽϣ���֤����
        this.setRs(2);
        this.setRt(this.chooseRt());
        this.setLabel(label);
        this.setText(this.createMIPSText());
    }

    @Override
    public String createMIPSText()
    {
        //bgtz rs, label
        return "bgtz $" + RegDic.RegName.get(this.getRs()) +
                ", " +
                this.getLabel();
    }
}

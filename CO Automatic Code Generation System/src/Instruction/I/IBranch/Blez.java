package Instruction.I.IBranch;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Blez extends IBranchInstruction
{
    public Blez(List<String> labelList, LinkedList<Integer> conflictReg)
    {
        this.setConflictReg(conflictReg);
        this.setOp(InstructionDic.BLEZ);
        this.setLabelList(labelList);
        this.setValue();
        labelList.add("blez");
    }

    public Blez(String label)
    {
        this.setOp(InstructionDic.BLEZ);
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
        //��Ϊv1��0��0����Сֵ�����бȽϣ���֤����
        Random random=new Random();
        if(random.nextBoolean())
        {
            this.setRs(3);
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
        //blez rs, label
        return "blez $" + RegDic.RegName.get(this.getRs()) +
                ", " +
                this.getLabel();
    }
}

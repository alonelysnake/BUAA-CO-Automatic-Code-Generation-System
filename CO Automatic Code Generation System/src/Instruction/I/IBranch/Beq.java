package Instruction.I.IBranch;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.List;

public class Beq extends IBranchInstruction
{
    /*
    private int range;//beq����ת���ܵ�ַ
    private int nowAddr;//��ǰ��ַ

    public Beq(int range, int nowAddr, List<String> labelList)
    {
        this.setOp(InstructionDic.BEQ);
        this.range = range;
        this.nowAddr = nowAddr;
        this.setLabelList(labelList);
    }
*/
    public Beq(List<String> labelList, LinkedList<Integer> conflictReg)
    {
        this.setConflictReg(conflictReg);
        this.setOp(InstructionDic.BEQ);
        this.setLabelList(labelList);
        this.setValue();
        labelList.add("beq");
    }

    //֪����ת��ǩʱ�Ĺ��췽��
    public Beq(String label, LinkedList<Integer> conflictReg)
    {
        this.setConflictReg(conflictReg);
        this.setOp(InstructionDic.BEQ);
        this.setValue(label);
    }

    @Override
    protected void setValue(String label)
    {
        this.setRs(this.chooseRs());
        this.setRt(this.getRs());
        this.setLabel(label);
        this.setText(this.createMIPSText());
    }

    @Override
    public String createMIPSText()
    {
        //beq rs, rt, label
        return "beq $" + RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getLabel();
    }
}

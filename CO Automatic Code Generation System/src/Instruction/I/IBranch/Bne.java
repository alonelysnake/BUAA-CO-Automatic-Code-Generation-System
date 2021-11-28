package Instruction.I.IBranch;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.List;

public class Bne extends IBranchInstruction
{
    /*
    final private int range;//beq����ת���ܵ�ַ
    final private int nowAddr;//��ǰ��ַ
    final private List<String> labelList;

    public Bne(int range, int nowAddr, List<String> labelList)
    {
        this.setOp(InstructionDic.BNE);
        this.range = range;
        this.nowAddr = nowAddr;
        this.labelList = labelList;
    }
    */
    public Bne(List<String> labelList, LinkedList<Integer> conflictReg)
    {
        this.setConflictReg(conflictReg);
        this.setOp(InstructionDic.BNE);
        this.setLabelList(labelList);
        this.setValue();
        labelList.add("bne");
    }

    //֪����ת��ǩʱ�Ĺ��췽��
    public Bne(String label)
    {
        this.setOp(InstructionDic.BNE);
        this.setValue(label);
    }

    @Override
    protected void setValue(String label)
    {
        //��Ϊv0��v1����Сֵ�����ֵ�����бȽ�
        this.setRs(2);
        this.setRt(3);
        this.setLabel(label);
        this.setText(this.createMIPSText());
    }

    @Override
    public String createMIPSText()
    {
        //bne rs, rt, label
        return "bne $" + RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getLabel();
    }
}

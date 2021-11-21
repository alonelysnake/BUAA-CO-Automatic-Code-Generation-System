package Instruction.I.IBranch;

import Instruction.InstructionDic;
import Instruction.RegDic;

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
    public Beq(List<String> labelList)
    {
        this.setOp(InstructionDic.BEQ);
        this.setLabelList(labelList);
        this.setValue();
    }

    //֪����ת��ǩʱ�Ĺ��췽��
    public Beq(String label)
    {
        this.setOp(InstructionDic.BEQ);
        this.setValue(label);
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

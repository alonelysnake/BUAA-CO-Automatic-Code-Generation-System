package Instruction.J;

import Instruction.Instruction;

import java.util.List;

abstract public class JInstruction extends Instruction
{
    private String imm26;
    private String label;
    private int nowAddr;//当前地址（跳转一定比当前地址大（暂时以此保证程序不会死循环））
    private List<String> labelList;//指令的标签集合

    public String getImm26()
    {
        return imm26;
    }

    public String getLabel()
    {
        return label;
    }

    public int getNowAddr()
    {
        return nowAddr;
    }

    public List<String> getLabelList()
    {
        return labelList;
    }

    public void setImm26(String imm26)
    {
        this.imm26 = imm26;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setLabelList(List<String> labelList)
    {
        this.labelList = labelList;
    }

    public void setNowAddr(int nowAddr)
    {
        this.nowAddr = nowAddr;
    }

    abstract protected String chooseImm26();

    abstract protected String chooseLabel();

    @Override
    public String createMachineCode()
    {
        String code;
        code = this.getOp() + this.getImm26();
        return code;
    }

    @Override
    protected void setValue()
    {
        this.setLabel(this.chooseLabel());
        this.setImm26(this.chooseImm26());
    }
}

package Instruction;

import java.util.LinkedList;

abstract public class Instruction
{
    private String op = "000000";
    private String machineCode;
    private String text;
    private LinkedList<Integer> ConflictReg;

    public String getOp()
    {
        return op;
    }

    public String getMachineCode()
    {
        return machineCode;
    }

    public String getText()
    {
        return text;
    }

    public LinkedList<Integer> getConflictReg()
    {
        return ConflictReg;
    }

    public void setOp(String op)
    {
        this.op = op;
    }

    public void setMachineCode(String machineCode)
    {
        this.machineCode = machineCode;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setConflictReg(LinkedList<Integer> conflictReg)
    {
        ConflictReg = conflictReg;
    }

    abstract public String createMachineCode();

    abstract public String createMIPSText();

    //设置各个变量的值
    abstract protected void setValue();
}

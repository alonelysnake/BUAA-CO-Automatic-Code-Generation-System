package Instruction;

abstract public class Instruction
{
    private String op;
    private String machineCode;
    private String text;

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

    abstract public String createMachineCode();

    abstract public String createMIPSText();

    //设置各个变量的值
    abstract protected void setValue();
}

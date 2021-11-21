package Instruction;

public class Nop extends Instruction
{

    public Nop()
    {
        this.setValue();
    }

    @Override
    public String createMachineCode()
    {
        return "00000000000000000000000000000000";
    }

    @Override
    public String createMIPSText()
    {
        return "nop";
    }

    @Override
    protected void setValue()
    {
        this.setText(this.createMIPSText());
    }
}

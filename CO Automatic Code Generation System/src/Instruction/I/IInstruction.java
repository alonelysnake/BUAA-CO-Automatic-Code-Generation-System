package Instruction.I;

import Instruction.Instruction;

import java.util.Set;

abstract public class IInstruction extends Instruction
{
    private int rs = 0;
    private int rt;
    private int imm16;//16位立即数
    private String label = null;//指令使用的标签
    private Set<Integer> writeProhibit;//禁止写入的寄存器编号
    private Set<Integer> hasVal;//有值的寄存器编号

    public int getImm16()
    {
        return imm16;
    }

    public int getRs()
    {
        return rs;
    }

    public int getRt()
    {
        return rt;
    }

    public String getLabel()
    {
        return label;
    }

    public Set<Integer> getWriteProhibit()
    {
        return writeProhibit;
    }

    public Set<Integer> getHasVal()
    {
        return hasVal;
    }

    public void setImm16(int imm16)
    {
        this.imm16 = imm16;
    }

    public void setRs(int rs)
    {
        this.rs = rs;
    }

    public void setRt(int rt)
    {
        this.rt = rt;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setWriteProhibit(Set<Integer> writeProhibit)
    {
        this.writeProhibit = writeProhibit;
    }

    public void setHasVal(Set<Integer> hasVal)
    {
        this.hasVal = hasVal;
    }

    abstract protected int chooseRs();

    abstract protected int chooseRt();

    abstract protected int chooseImm16();

    abstract protected String chooseLabel();

    @Override
    public String createMachineCode()
    {
        String code;
        code = this.getOp() + this.getRs() + this.getRt() + this.getImm16();
        return code;
    }
}

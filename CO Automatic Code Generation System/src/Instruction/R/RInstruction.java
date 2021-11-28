package Instruction.R;

import Instruction.Instruction;

import java.util.List;
import java.util.Set;

abstract public class RInstruction extends Instruction
{
    private int rs;
    private int rt;
    private int rd;
    private int shamt;
    private String func;
    private Set<Integer> writeProhibit;//禁止写入的寄存器编号
    private Set<Integer> hasVal;//已经有值的寄存器编号（适合用来运算）

    public int getRs()
    {
        return rs;
    }

    public int getRt()
    {
        return rt;
    }

    public int getRd()
    {
        return rd;
    }

    public int getShamt()
    {
        return shamt;
    }

    public String getFunc()
    {
        return func;
    }

    public Set<Integer> getHasVal()
    {
        return hasVal;
    }

    public Set<Integer> getWriteProhibit()
    {
        return writeProhibit;
    }

    public void setRd(int rd)
    {
        this.rd = rd;
    }

    public void setRs(int rs)
    {
        this.rs = rs;
    }

    public void setRt(int rt)
    {
        this.rt = rt;
    }

    public void setShamt(int shamt)
    {
        this.shamt = shamt;
    }

    public void setFunc(String func)
    {
        this.func = func;
    }

    public void setHasVal(Set<Integer> hasVal)
    {
        this.hasVal = hasVal;
    }

    public void setWriteProhibit(Set<Integer> writeProhibit)
    {
        this.writeProhibit = writeProhibit;
    }

    //选择rs的编号
    abstract protected int chooseRs();

    //选择rt的编号
    abstract protected int chooseRt();

    //选择rd的编号
    abstract protected int chooseRd();

    //选择shamt的值
    abstract protected int chooseShamt();

    @Override
    public void setValue()
    {
        this.setRd(this.chooseRd());
        this.setRs(this.chooseRs());
        this.setRt(this.chooseRt());
        this.setShamt(this.chooseShamt());
        this.setText(this.createMIPSText());
    }

    @Override
    public String createMachineCode()
    {
        String code;
        code = this.getOp() + this.getRs() + this.getRt() + this.getRd() + this.getShamt() + this.getFunc();
        return code;
    }
}

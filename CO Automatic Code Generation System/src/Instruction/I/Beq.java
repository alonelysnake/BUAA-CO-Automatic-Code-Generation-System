package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;

public class Beq extends IInstruction
{
    private int range;//beq可跳转的总地址
    private int nowAddr;//当前地址
    private List<String> labelList;

    public Beq(int range, int nowAddr, List<String> labelList)
    {
        this.setOp(InstructionDic.BEQ);
        this.range = range;
        this.nowAddr = nowAddr;
        this.labelList = labelList;
    }

    public Beq(String label)
    {
        this.setLabel(label);
    }

    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);

        return random.nextInt(28);
    }

    @Override
    protected int chooseRt()
    {
        Random random = new Random();
        if (random.nextBoolean())
        {
            return this.chooseRs();
        } else
        {
            return this.getRs();
        }
    }

    @Override
    protected String chooseImm16()
    {
        return null;
    }

    @Override
    protected String chooseLabel()
    {
        return "label" + (this.labelList.size() * 2 + 1);
    }

    @Override
    public String createMIPSText()
    {
        if (this.getLabel() == null)
        {
            //不知道分支将会到达哪里时所用
            super.createMIPSText();
            //beq rs, rt, label
        } else
        {
            //确定分支将会跳转的位置（需要保证分支进行跳转）
            this.setRs(this.chooseRs());
            this.setRt(this.getRs());
        }
        return "beq $" + RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getLabel();
    }
}

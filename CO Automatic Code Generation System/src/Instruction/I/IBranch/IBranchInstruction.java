package Instruction.I.IBranch;

import Instruction.I.IInstruction;

import java.util.List;
import java.util.Random;

abstract public class IBranchInstruction extends IInstruction
{
    private List<String> labelList;

    public List<String> getLabelList()
    {
        return labelList;
    }

    public void setLabelList(List<String> labelList)
    {
        this.labelList = labelList;
    }

    //默认为随机选取
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(28);
    }

    //默认为一定概率一定跳转，一定概率极大可能不跳转。当前设置比例为1：1
    @Override
    protected int chooseRt()
    {
        Random random = new Random();
        if (random.nextBoolean())
        {
            return random.nextInt(28);
        } else
        {
            return this.getRs();
        }
    }

    //目前分支指令不需要使用imm，如果要实现直接生成机器码的功能则需要实现该方法
    @Override
    protected int chooseImm16()
    {
        return 0;
    }

    @Override
    protected String chooseLabel()
    {
        return "label" + (this.labelList.size() * 2 + 1);
    }

    //默认跳转
    @Override
    protected void setValue()
    {
        this.setRs(this.chooseRs());
        this.setRt(this.chooseRt());
        this.setLabel(this.chooseLabel());
        this.setText(this.createMIPSText());
    }

    //知道label指向哪里时一定发生跳转
    protected void setValue(String label)
    {
        this.setRs(this.chooseRs());
        this.setRt(this.getRs());
        this.setLabel(label);
        this.setText(this.createMIPSText());
    }
}

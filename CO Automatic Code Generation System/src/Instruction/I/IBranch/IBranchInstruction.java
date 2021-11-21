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

    //Ĭ��Ϊ���ѡȡ
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(28);
    }

    //Ĭ��Ϊһ������һ����ת��һ�����ʼ�����ܲ���ת����ǰ���ñ���Ϊ1��1
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

    //Ŀǰ��ָ֧���Ҫʹ��imm�����Ҫʵ��ֱ�����ɻ�����Ĺ�������Ҫʵ�ָ÷���
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

    //Ĭ����ת
    @Override
    protected void setValue()
    {
        this.setRs(this.chooseRs());
        this.setRt(this.chooseRt());
        this.setLabel(this.chooseLabel());
        this.setText(this.createMIPSText());
    }

    //֪��labelָ������ʱһ��������ת
    protected void setValue(String label)
    {
        this.setRs(this.chooseRs());
        this.setRt(this.getRs());
        this.setLabel(label);
        this.setText(this.createMIPSText());
    }
}

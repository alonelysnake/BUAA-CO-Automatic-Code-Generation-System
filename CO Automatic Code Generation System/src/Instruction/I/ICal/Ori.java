package Instruction.I.ICal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Ori extends ICalInstruction
{
    public Ori(Set<Integer> writeProhibit, Set<Integer> hasVal)
    {
        this.setOp(InstructionDic.ORI);
        this.setWriteProhibit(writeProhibit);
        this.setValue();
        hasVal.add(this.getRt());
    }

    //ָ����rs��imm
    public Ori(Set<Integer> writeProhibit, int rs, String imm)
    {
        this.setOp(InstructionDic.ORI);
        this.setWriteProhibit(writeProhibit);
        this.setValue(rs, imm);
    }

    //ָ����rs��rt�Ĺ��췽ʽ
    public void setValue(int rs, String imm)
    {
        this.setRs(rs);
        this.setRt(this.chooseRt());
        this.setImm16(0);
        this.setText(this.createMIPSText());
    }

    //Ori��������ɲ���Ҫrs��ֵ
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(28);
    }

    @Override
    public String createMIPSText()
    {
        //ori rt, rs, imm16 ��16���Ʊ�ʾ
        return "ori $" + RegDic.RegName.get(this.getRt()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", 0x" +
                Integer.toHexString(this.getImm16());
    }
}

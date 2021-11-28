package Instruction.I.ICal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Ori extends ICalInstruction
{
    //随机
    public Ori(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setOp(InstructionDic.ORI);
    }

    //指定了rs，imm
    public Ori(Set<Integer> writeProhibit, LinkedList<Integer> conflictReg, int rs, String imm)
    {
        this.setOp(InstructionDic.ORI);
        this.setWriteProhibit(writeProhibit);
        this.setValue(rs, imm);
        conflictReg.addLast(this.getRt());
        if (conflictReg.size() > 3)
        {
            conflictReg.removeFirst();
        }
    }

    //指定了rs，rt的构造方式
    public void setValue(int rs, String imm)
    {
        this.setRs(rs);
        this.setRt(this.chooseRt());
        this.setImm16(0);
        this.setText(this.createMIPSText());
    }

    //Ori的随机生成不需要rs含值
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int size = this.getConflictReg().size();
        if (size < 3 || random.nextInt(10) < 5)
        {
            return random.nextInt(28);
        } else
        {
            int index = random.nextInt(size);
            return this.getConflictReg().get(index);
        }
    }

    @Override
    public String createMIPSText()
    {
        //ori rt, rs, imm16 用16进制表示
        return "ori $" + RegDic.RegName.get(this.getRt()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", 0x" +
                Integer.toHexString(this.getImm16());
    }
}

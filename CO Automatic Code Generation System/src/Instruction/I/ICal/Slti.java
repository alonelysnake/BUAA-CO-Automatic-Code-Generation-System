package Instruction.I.ICal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Slti extends ICalInstruction
{
    public Slti(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setOp(InstructionDic.SLTI);
    }

    //考虑测试一定相等、一定小于/大于的情况（用$0,$2,$3寄存器）
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int sel = random.nextInt(4);
        switch (sel)
        {
            case 0:
                //恒等
                return 0;
            case 1:
                //恒大
                return 2;
            case 2:
                //恒小
                return 3;
            default:
                //随机
                return random.nextInt(28);
        }
    }

    //考虑相等时和0寄存器的联动
    @Override
    protected int chooseImm16()
    {
        if (this.getRs() == 0)
        {
            return 0;
        } else
        {
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            return random.nextInt(32767);
        }
    }

    @Override
    public String createMIPSText()
    {
        //slti rt, rs, imm16 用16进制表示
        return "slti $" + RegDic.RegName.get(this.getRt()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", 0x" +
                Integer.toHexString(this.getImm16());
    }
}

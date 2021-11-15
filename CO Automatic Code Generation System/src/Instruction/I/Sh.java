package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Sh extends IInstruction
{
    private final Set<Integer> hasVal;

    public Sh(Set<Integer> hasVal)
    {
        this.setOp(InstructionDic.SH);
        this.hasVal = hasVal;
    }

    @Override
    protected int chooseRs()
    {
        return 0;
    }

    @Override
    protected int chooseRt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rt;
        int cnt = 0;//检测是否有被修改过的寄存器
        while (cnt < 100)
        {
            rt = random.nextInt(28);
            //进行赋值的寄存器必须是有值的
            if (this.hasVal.contains(rt))
            {
                return rt;
            }
            cnt++;
        }
        System.out.println("所有寄存器均未赋值");
        System.exit(0);
        return 0;
    }

    @Override
    protected String chooseImm16()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int imm = random.nextInt(100) * 2;
        return Integer.toString(imm);
    }

    @Override
    protected String chooseLabel()
    {
        return null;
    }

    @Override
    public String createMIPSText()
    {
        super.createMIPSText();
        //sh rt, imm16(rs)  固定存储地址为50个字,rs=0,要求GPR[rt]!=0
        return "sh $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                "($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}

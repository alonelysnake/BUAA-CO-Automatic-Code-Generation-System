package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Lui extends IInstruction
{
    public Lui(Set<Integer> writeProhibit)
    {
        this.setOp(InstructionDic.LUI);
        this.setWriteProhibit(writeProhibit);
    }

    @Override
    protected int chooseRs()
    {
        return 0;
    }

    @Override
    protected int chooseRt()
    {
        int seed=new Random().nextInt();
        Random random=new Random(seed);
        int rt;
        int cnt=0;//检测寄存器是否可修改
        while(cnt<100)
        {
            rt=random.nextInt(28);
            //被赋值的寄存器必须是可修改的
            if(!this.getWriteProhibit().contains(rt))
            {
                return rt;
            }
            cnt++;
        }
        System.out.println("所有寄存器均不可再使用");
        System.exit(0);
        return 0;
    }

    @Override
    protected String chooseImm16()
    {
        int seed=new Random().nextInt();
        Random random=new Random(seed);
        int imm=random.nextInt(65535);
        return Integer.toHexString(imm);
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
        //lui rt, imm   用16进制表示
        return "lui $" + RegDic.RegName.get(this.getRt()) +
                ", 0x" +
                this.getImm16();
    }
}

package Instruction.R;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Sll extends RInstruction
{
    public Sll(Set<Integer> writeProhibit, Set<Integer> hasVal)
    {
        this.setFunc(InstructionDic.SLL);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
    }

    @Override
    protected int chooseRd()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rd;
        int cnt = 0;//检测寄存器是否可修改
        while (cnt < 100)
        {
            rd = random.nextInt(28);
            //被赋值的寄存器必须是可修改的
            if (!this.getWriteProhibit().contains(rd))
            {
                return rd;
            }
            cnt++;
        }
        System.out.println("所有寄存器均不可再使用");
        System.exit(0);
        return 0;
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
        int cnt = 0;//检测寄存器是否可修改
        while (cnt < 100)
        {
            rt = random.nextInt(28);
            //被赋值的寄存器必须是有值的
            if (this.getHasVal().contains(rt))
            {
                return rt;
            }
            cnt++;
        }
        System.out.println("无已赋值寄存器");
        System.exit(0);
        return 0;
    }

    @Override
    protected int chooseShamt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(31);
    }

    @Override
    public String createMIPSText()
    {
        super.createMIPSText();

        //sll rd, rt, shamt
        return "sll $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRt()) +
                ", " +
                getShamt();
    }
}

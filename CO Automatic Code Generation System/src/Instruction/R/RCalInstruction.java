package Instruction.R;

import java.util.Random;

abstract public class RCalInstruction extends RInstruction
{
    //默认可写中随机
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

    //默认随机
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rs;
        int cnt = 0;//检测寄存器是否有值
        while (cnt < 100)
        {
            rs = random.nextInt(28);
            //被赋值的寄存器必须是有值的
            if (this.getHasVal().contains(rs))
            {
                return rs;
            }
            cnt++;
        }
        System.out.println("无已赋值寄存器");
        System.exit(0);
        return 0;
    }

    //默认随机
    @Override
    protected int chooseRt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rt;
        int cnt = 0;//检测寄存器是否有值
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

    //默认为0
    @Override
    protected int chooseShamt()
    {
        return 0;
    }
}

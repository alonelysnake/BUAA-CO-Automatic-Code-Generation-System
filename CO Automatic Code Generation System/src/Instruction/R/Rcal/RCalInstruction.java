package Instruction.R.Rcal;

import Instruction.R.RInstruction;
import Interface.RandomReg;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

abstract public class RCalInstruction extends RInstruction implements RandomReg
{
    public RCalInstruction()
    {
    }

    //随机生成
    public RCalInstruction(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setConflictReg(conflictReg);

        this.setValue();

        if (this.getRd() != 0)
        {
            hasVal.add(this.getRd());
            conflictReg.addLast(this.getRd());
            if (conflictReg.size() > 3)
            {
                conflictReg.removeFirst();
            }
        }
    }

    //默认可写中随机
    @Override
    protected int chooseRd()
    {
        return randomWriteReg(this.getWriteProhibit());
    }

    //默认随机
    @Override
    protected int chooseRs()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int size = this.getConflictReg().size();
        if (size < 3 || random.nextInt(10) < 2)
        {
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
        int index = random.nextInt(size);
        return this.getConflictReg().get(index);
    }

    //曾经的随机赋值（不易满足转发阻塞需求）
/*
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
*/
    //默认随机
    @Override
    protected int chooseRt()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int size = this.getConflictReg().size();
        if (size < 3 || random.nextInt(10) < 2)
        {
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
        int index = random.nextInt(size);
        //再次随机降低相等概率
        if (this.getConflictReg().get(index) == this.getRs())
        {
            index = random.nextInt(size);
        }
        return this.getConflictReg().get(index);
    }

    //默认为0
    @Override
    protected int chooseShamt()
    {
        return 0;
    }
}

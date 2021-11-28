package Instruction.I.ICal;

import Instruction.I.IInstruction;
import Interface.RandomReg;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

abstract public class ICalInstruction extends IInstruction implements RandomReg
{
    public ICalInstruction()
    {

    }

    public ICalInstruction(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
        this.setConflictReg(conflictReg);

        this.setValue();

        hasVal.add(this.getRt());
        conflictReg.addLast(this.getRt());
        if (conflictReg.size() > 3)
        {
            conflictReg.removeFirst();
        }
    }

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

    @Override
    protected int chooseRt()
    {
        return randomWriteReg(this.getWriteProhibit());
    }

    @Override
    protected int chooseImm16()
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        return random.nextInt(65535);
    }

    @Override
    protected String chooseLabel()
    {
        return null;
    }

    @Override
    protected void setValue()
    {
        this.setRs(this.chooseRs());
        this.setRt(this.chooseRt());
        this.setImm16(this.chooseImm16());
        this.setText(this.createMIPSText());
    }
}

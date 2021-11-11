package Instruction.I;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Lw extends IInstruction
{
    private List<Integer> addrList;//已经写入的地址

    public Lw(List<Integer> addrList, Set<Integer> writeProhibit)
    {
        this.setOp(InstructionDic.LW);
        this.addrList = addrList;
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
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rt;
        int cnt = 0;//检测寄存器是否可修改
        while (cnt < 100)
        {
            rt = random.nextInt(28);
            //被赋值的寄存器必须是可修改的
            if (!this.getWriteProhibit().contains(rt))
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
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int index = random.nextInt(addrList.size());
        int imm = addrList.get(index);
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
        //lw rt, imm16(rs)  固定rs为0
        return "lw $" + RegDic.RegName.get(this.getRt()) +
                ", " +
                this.getImm16() +
                ", ($" +
                RegDic.RegName.get(this.getRs()) +
                ")";
    }
}

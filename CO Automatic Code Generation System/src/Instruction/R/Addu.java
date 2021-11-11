package Instruction.R;


import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.Random;
import java.util.Set;

public class Addu extends RInstruction
{
    /*
    * 构造方法
    * 传入：
    * 禁止修改的寄存器（运算指令不能用的，包含at这种和存了跳转指令（用于jr）的），默认：0（因为写了也没用），1，26-31
    * 有值的寄存器（用于各种基本运算）（0也可以包含进来）
    * */
    public Addu(Set<Integer> writeProhibit, Set<Integer>hasVal)
    {
        this.setFunc(InstructionDic.ADDU);
        this.setWriteProhibit(writeProhibit);
        this.setHasVal(hasVal);
    }

    @Override
    protected int chooseRd()
    {
        int seed=new Random().nextInt();
        Random random=new Random(seed);
        int rd;
        int cnt=0;//检测寄存器是否可修改
        while(cnt<100)
        {
            rd=random.nextInt(28);
            //被赋值的寄存器必须是可修改的
            if(!this.getWriteProhibit().contains(rd))
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
        int seed=new Random().nextInt();
        Random random=new Random(seed);
        int rs;
        int cnt=0;//检测寄存器是否可修改
        while(cnt<100)
        {
            rs=random.nextInt(28);
            //被赋值的寄存器必须是有值的
            if(this.getHasVal().contains(rs))
            {
                return rs;
            }
            cnt++;
        }
        System.out.println("无已赋值寄存器");
        System.exit(0);
        return 0;
    }

    @Override
    protected int chooseRt()
    {
        return this.chooseRs();
    }

    @Override
    protected int chooseShamt()
    {
        return 0;
    }

    @Override
    public String createMIPSText()
    {
        super.createMIPSText();

        //addu rd, rs, rt
        return "addu $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

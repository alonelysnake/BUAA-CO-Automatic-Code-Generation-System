package Interface;

import java.util.Random;
import java.util.Set;

//（在可写入寄存器中）生成完全随机的寄存器编号
public interface RandomReg
{
    public default int randomWriteReg(Set<Integer> writeProhibit)
    {
        int seed = new Random().nextInt();
        Random random = new Random(seed);
        int rd;
        int cnt = 0;//检测寄存器是否可修改
        while (cnt < 100)
        {
            rd = random.nextInt(28);
            //被赋值的寄存器必须是可修改的
            if (!writeProhibit.contains(rd))
            {
                return rd;
            }
            cnt++;
        }
        System.out.println("所有寄存器均不可再使用");
        System.exit(0);
        return 0;
    }

    public default int randomReadReg()
    {
        System.out.println("randomReadReg尚未实现");
        return 0;
    }

    public default int conflictReadReg()
    {
        System.out.println("conflictReadReg尚未实现");
        return 0;
    }
}

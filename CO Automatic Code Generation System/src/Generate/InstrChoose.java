package Generate;

import Instruction.I.IBranch.*;
import Instruction.I.ICal.*;
import Instruction.I.IInstruction;
import Instruction.I.ILS.*;
import Instruction.R.RInstruction;
import Instruction.R.RMulDiv.*;
import Instruction.R.Rcal.*;
import Exception.UnknownInstrException;

import java.util.*;

public class InstrChoose
{

    public static RInstruction chooseRCal(Set<String> RCal,
                                          Set<Integer> writeProhibit,
                                          HashSet<Integer> hasVal,
                                          LinkedList<Integer> conflictReg) throws UnknownInstrException
    {
        int index;
        Random random = new Random();
        List<String> RList = new ArrayList<>(RCal);

        index = random.nextInt(RList.size());
        String instruction = RList.get(index);
        switch (instruction)
        {
            case "addu":
                return new Addu(writeProhibit, hasVal, conflictReg);
            case "and":
                return new And(writeProhibit, hasVal, conflictReg);
            case "nor":
                return new Nor(writeProhibit, hasVal, conflictReg);
            case "subu":
                return new Subu(writeProhibit, hasVal, conflictReg);
            case "or":
                return new Or(writeProhibit, hasVal, conflictReg);
            case "xor":
                return new Xor(writeProhibit, hasVal, conflictReg);
            case "sll":
                return new Sll(writeProhibit, hasVal, conflictReg);
            case "sllv":
                return new Sllv(writeProhibit, hasVal, conflictReg);
            case "sra":
                return new Sra(writeProhibit, hasVal, conflictReg);
            case "srav":
                return new Srav(writeProhibit, hasVal, conflictReg);
            case "srl":
                return new Srl(writeProhibit, hasVal, conflictReg);
            case "srlv":
                return new Srlv(writeProhibit, hasVal, conflictReg);
            case "slt":
                return new Slt(writeProhibit, hasVal, conflictReg);
            default:
                throw new UnknownInstrException(instruction);
        }
    }

    public static IInstruction chooseICal(Set<String> ICal,
                                          Set<Integer> writeProhibit,
                                          HashSet<Integer> hasVal,
                                          LinkedList<Integer> conflictReg) throws UnknownInstrException
    {
        int index;
        List<String> IList = new ArrayList<>(ICal);
        Random random = new Random();
        index = random.nextInt(IList.size());
        String instruction = IList.get(index);
        switch (instruction)
        {
            case "lui":
                return new Lui(writeProhibit, hasVal, conflictReg);
            case "ori":
                return new Ori(writeProhibit, hasVal, conflictReg);
            case "addi":
                return new Addi(writeProhibit, hasVal, conflictReg);
            case "andi":
                return new Andi(writeProhibit, hasVal, conflictReg);
            case "slti":
                return new Slti(writeProhibit, hasVal, conflictReg);
            case "xori":
                return new Xori(writeProhibit, hasVal, conflictReg);
            default:
                throw new UnknownInstrException(instruction);
        }
    }

    public static IInstruction chooseLS(Set<String> LS,
                                        List<Integer> addrList,
                                        Set<Integer> writeProhibit,
                                        HashSet<Integer> hasVal,
                                        LinkedList<Integer> conflictReg) throws UnknownInstrException
    {
        int index;
        List<String> LSList = new ArrayList<>(LS);
        Random random = new Random();
        index = random.nextInt(LSList.size());
        String instruction = LSList.get(index);

        switch (instruction)
        {
            case "lw":
                return new Lw(addrList, writeProhibit, hasVal, conflictReg);
            case "lh":
                return new Lh(addrList, writeProhibit, hasVal, conflictReg);
            case "lb":
                return new Lb(addrList, writeProhibit, hasVal, conflictReg);
            case "sw":
                return new Sw(hasVal, addrList);
            case "sh":
                return new Sh(hasVal, addrList);
            case "sb":
                return new Sb(hasVal, addrList);
            default:
                throw new UnknownInstrException(instruction);
        }
    }

    public static IInstruction chooseBranch(Set<String> branch,
                                            List<String> labelList,
                                            LinkedList<Integer> conflictReg) throws UnknownInstrException
    {
        int index;
        List<String> branchList = new ArrayList<>(branch);
        Random random = new Random();
        index = random.nextInt(branchList.size());
        String instruction = branchList.get(index);
        switch (instruction)
        {
            case "beq":
                return new Beq(labelList, conflictReg);
            case "bne":
                return new Bne(labelList, conflictReg);
            case "bgez":
                return new Bgez(labelList, conflictReg);
            case "blez":
                return new Blez(labelList, conflictReg);
            case "bgtz":
                return new Bgtz(labelList, conflictReg);
            case "bltz":
                return new Bltz(labelList, conflictReg);
            default:
                System.out.println("未知指令" + instruction);
                throw new UnknownInstrException(instruction);
        }
    }

    public static RInstruction chooseMD(Set<String> MD,
                                        int rs,
                                        int rt,
                                        Set<Integer> writeProhibit,
                                        HashSet<Integer> hasVal,
                                        LinkedList<Integer> conflictReg) throws UnknownInstrException
    {
        Random random = new Random();
        //限定乘除法不为0
        if (rs != 0 && rt != 0)
        {
            if (random.nextBoolean())
            {
                //乘法
                return new Mult(rs, rt);
            } else
            {
                //除法
                return new Div(rs, rt);
            }
        }
        //存在0，则不为乘除法运算
        int index, cnt = 10;
        String instruction = "mult";
        List<String> MDList = new ArrayList<>(MD);
        while ((instruction.equals("mult") || instruction.equals("div")) && cnt > 0)
        {
            index = random.nextInt(MDList.size());
            instruction = MDList.get(index);
            cnt--;//防止死循环
        }
        switch (instruction)
        {
            case "mfhi":
                return new Mfhi(writeProhibit, hasVal, conflictReg);
            case "mflo":
                return new Mflo(writeProhibit, hasVal, conflictReg);
            case "mthi":
                return new Mthi(writeProhibit, hasVal, conflictReg);
            case "mtlo":
                return new Mtlo(writeProhibit, hasVal, conflictReg);
            case "mult":
            case "div":
                System.out.println("测试指令集内不存在非乘除运算的乘除相关指令");
                return new Mult(0, 0);
            default:
                throw new UnknownInstrException(instruction);
        }
    }
}

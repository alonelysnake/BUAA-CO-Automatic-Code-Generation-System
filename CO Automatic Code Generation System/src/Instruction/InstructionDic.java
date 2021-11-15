package Instruction;

import java.util.HashSet;
import java.util.Set;

public class InstructionDic
{
    final static public String ADDU = "100001";
    final static public String SUBU = "100011";
    final static public String AND = "100100";
    final static public String OR = "100101";
    final static public String SLL = "000000";
    final static public String SLLV = "000100";
    final static public String SLT = "101010";
    final static public String JALR = "001001";
    final static public String JR = "000011";

    final static public String J = "000010";
    final static public String JAL = "000011";

    final static public String LW = "100011";
    final static public String LH = "100001";
    final static public String LB = "100000";
    final static public String SW = "101011";
    final static public String SH = "101001";
    final static public String SB = "101000";
    final static public String ORI = "001101";
    final static public String LUI = "001111";
    final static public String BEQ = "000100";
    final static public String BNE = "000101";

    final static public Set<String> RMODE = new HashSet<>();
    final static public Set<String> IMODE = new HashSet<>();
    final static public Set<String> JMODE = new HashSet<>();
    final static public Set<String> BRANCH = new HashSet<>();

    static
    {
        //保存所有已实现指令

        RMODE.add("addu");
        RMODE.add("subu");
        RMODE.add("jr");
        //RMODE.add("jalr");

        IMODE.add("ori");
        IMODE.add("lui");
        IMODE.add("lw");
        IMODE.add("sw");
        IMODE.add("beq");

        BRANCH.add("beq");

        JMODE.add("j");
        JMODE.add("jal");
    }
}

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

    final static public String MFHI = "010000";
    final static public String MFLO = "010010";
    final static public String MTHI = "010001";
    final static public String MTLO = "010011";
    final static public String MULT = "011000";
    final static public String DIV = "011010";

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
    final static public String NOR = "100111";
    final static public String SLTI = "001010";
    final static public String SRA = "000011";
    final static public String SRAV = "000111";
    final static public String SRL = "000010";
    final static public String SRLV = "000110";
    final static public String XOR = "100110";
    final static public String XORI = "001110";
    final static public String ANDI = "001100";
    final static public String ADDI = "001000";

    final static public String BEQ = "000100";
    final static public String BNE = "000101";
    final static public String BLTZ = "000001";
    final static public String BLEZ = "000110";
    final static public String BGEZ = "000001";
    final static public String BGTZ = "000111";

    final static public Set<String> RCALMODE = new HashSet<>();
    final static public Set<String> ICALMODE = new HashSet<>();
    final static public Set<String> RJMODE = new HashSet<>();
    final static public Set<String> LSMODE = new HashSet<>();
    final static public Set<String> JMODE = new HashSet<>();
    final static public Set<String> BRANCH = new HashSet<>();
    final static public Set<String> MDMODE = new HashSet<>();

    static
    {
        //保存所有已实现指令
        RCALMODE.add("addu");
        RCALMODE.add("and");
        RCALMODE.add("nor");
        RCALMODE.add("subu");
        RCALMODE.add("or");
        RCALMODE.add("xor");
        RCALMODE.add("sra");
        RCALMODE.add("srav");
        RCALMODE.add("srl");
        RCALMODE.add("srlv");
        RCALMODE.add("sll");
        RCALMODE.add("sllv");
        RCALMODE.add("slt");
        RJMODE.add("jr");
        RJMODE.add("jalr");

        ICALMODE.add("ori");
        ICALMODE.add("lui");
        ICALMODE.add("addi");
        ICALMODE.add("andi");
        ICALMODE.add("slti");
        ICALMODE.add("xori");

        LSMODE.add("lw");
        LSMODE.add("lh");
        LSMODE.add("lb");
        LSMODE.add("sw");
        LSMODE.add("sh");
        LSMODE.add("sb");

        BRANCH.add("beq");
        BRANCH.add("bne");
        BRANCH.add("bgez");
        BRANCH.add("blez");
        BRANCH.add("bgtz");
        BRANCH.add("bltz");

        JMODE.add("j");
        JMODE.add("jal");

        MDMODE.add("mult");
        MDMODE.add("div");
        MDMODE.add("mfhi");
        MDMODE.add("mflo");
        MDMODE.add("mthi");
        MDMODE.add("mtlo");
    }
}

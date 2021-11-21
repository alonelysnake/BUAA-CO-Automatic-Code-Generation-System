package Generate;

import Instruction.I.*;
import Instruction.I.IBranch.Beq;
import Instruction.I.IBranch.Bne;
import Instruction.I.ICal.Lui;
import Instruction.I.ICal.Ori;
import Instruction.I.ILS.*;
import Instruction.J.J;
import Instruction.J.JInstruction;
import Instruction.J.Jal;
import Instruction.R.*;
import Exception.UnknownInstrException;

import java.util.*;

public class Generate
{
    private int instructionNum;//ָ������
    private int cnt = 0;//��ǰָ��index
    final private int labelBlockSize = 3;//label���С
    private Set<String> R;//ѡ���Rָ��
    private Set<String> I;//ѡ���Iָ�����֧��
    private Set<String> JR;//jalr,jr
    private Set<String> LS;//load/saveָ��
    private Set<String> J;//ʵ��jump��ָ�j��jal��jalr��jr��
    private Set<String> branch;//I�еķ�ָ֧��

    final private int SEED = 114514;//��������������
    Random random = new Random(SEED);

    RInstruction rInstruction;
    IInstruction iInstruction;
    JInstruction jInstruction;

    HashSet<Integer> writeProhibit = new HashSet<>();//������������д��ļĴ�����ϵͳ�ú�jr/jalr�õģ�
    HashSet<Integer> hasVal = new HashSet<>();//��ֵ�ļĴ���
    List<Integer> addrList = new ArrayList<>();//��ֵ�Ĵ洢����ַ
    List<String> labelList = new ArrayList<>();//�洢��ָ֧��ĳ���˳��

    List<String> ans = new ArrayList<>();//������ַ�������

    public Generate(int instructionNum, Set<String> R, Set<String> I, Set<String> branch, Set<String> J)
    {
        this.setInstructionNum(instructionNum);
        this.setR(R);
        this.setI(I);
        this.setBranch(branch);
        this.setJ(J);
        this.initWriteProhibit();
    }

    private void initWriteProhibit()
    {
        writeProhibit.add(0);
        writeProhibit.add(1);
        writeProhibit.add(2);
        writeProhibit.add(3);
        writeProhibit.add(26);
        writeProhibit.add(27);
        writeProhibit.add(28);
        writeProhibit.add(29);
        writeProhibit.add(30);
        writeProhibit.add(31);
    }

    //grf��dm����ֵ
    private void initReg()
    {
        //�Ĵ�������ʼֵ
        this.hasVal.add(0);
        this.hasVal.add(2);
        this.hasVal.add(3);
        //$v0($2)ֵ�̶�Ϊ2^32-1
        this.ans.add("lui $v0, 0x7fff");
        this.ans.add("ori $v0, $v0, 0xffff");
        //$v1($3)ֵ�̶�Ϊ-2^32
        this.ans.add("lui $v1, 0xffff");
        this.ans.add("ori $v1, $v1, 0xffff");
        //��������Ĵ�����ֵ
        for (int i = 0; i < 3; i++)
        {
            iInstruction = new Ori(writeProhibit, this.hasVal);
            this.ans.add(iInstruction.createMIPSText());
            //������ֵ�ļĴ���
            hasVal.add(iInstruction.getRt());
            this.cnt++;
        }
        //�洢����ֵ���������ȡָ����ԣ�
        for (int i = 0; i < 3; i++)
        {
            this.iInstruction = new Sw(this.hasVal, this.addrList);
            this.ans.add(iInstruction.createMIPSText());
            //this.addrList.add(Integer.valueOf(iInstruction.getImm16()));
            this.cnt++;
        }
    }

    //��������label����
    private void createLabel()
    {
        //�������label��
        int size = this.labelList.size() * 2;//label�����Ŀ
        for (int i = 0; i < size; i++)
        {
            //������label 'i' :
            String text = "label" + i + ":";
            ans.add(text);

            //�����ɹ̶���Ŀ������Ƿ�֧��תָ��
            createRandomBlock();

            //��������ת������ָ�����;���������תλ��
            //��ͬ��֧��תָ�ͬ����
            String instr = this.labelList.get(i / 2);
            if (instr.equals("jal"))
            {
                //һ������ִ��jalr�����Ҫ���ԣ�
                boolean jalr = this.random.nextBoolean();
                if (this.R.contains("jalr") && jalr)
                {
                    //δʵ��
                    System.out.println("δʵ��");
                } else
                {
                    //���Կ���ori�������Ĵ���Ȼ��jr�Ǹ�
                    //ori
                    iInstruction = new Ori(this.writeProhibit, 31, "0");
                    ans.add(iInstruction.createMIPSText());
                    //jr
                    rInstruction = new Jr(iInstruction.getRt(), this.writeProhibit);
                    ans.add(rInstruction.createMIPSText());
                }
            } else if (instr.equals("jalr"))
            {
                System.out.println("δʵ��");
            } else
            {
                //��ת�ı�ǩֵ
                //����ģ��ʵ��ͳһ����ת��ʽ����3��1��
                String label;
                if (i % 2 == 0)
                {
                    //label+3
                    label = "label" + (i + 3);
                } else
                {
                    //label-1
                    label = "label" + (i - 1);
                }

                if (instr.equals("j"))
                {
                    jInstruction = new J(label);
                    ans.add(jInstruction.createMIPSText());
                } else if (this.branch.contains(instr))
                {
                    //beq��bne��bgez����
                    switch (instr)
                    {
                        case "beq":
                            iInstruction = new Beq(label);
                            break;
                        case "bne":
                            break;
                    }

                    ans.add(iInstruction.createMIPSText());
                } else
                {
                    System.out.println(" ��( �� �� ��|||)��  ���ڷ�֧��ת�����ֵ�ָ����");
                    System.out.println("����ָ�" + instr);
                    jInstruction = new J("end");
                    ans.add(jInstruction.getText());
                }
                //�ӳٲ�
                addDelaySlot();
            }
        }

        //�������е�label
        ans.add("label" + (size + 1) + ":");
    }

    //�������label���ڵ�ָ��
    private void createRandomBlock()
    {
        for (int i = 0; i < this.labelBlockSize; i++)
        {
            int mode = this.random.nextInt(2);
            if (mode == 0)
            {
                //Rָ��
                try
                {
                    addR();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            } else
            {
                //Iָ��
                try
                {
                    addI();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            }
        }
    }

    public int getInstructionNum()
    {
        return instructionNum;
    }

    public Set<String> getR()
    {
        return R;
    }

    public Set<String> getI()
    {
        return I;
    }

    public Set<String> getJ()
    {
        return J;
    }

    public void setInstructionNum(int instructionNum)
    {
        this.instructionNum = instructionNum;
    }

    public void setR(Set<String> r)
    {
        R = r;
    }

    public void setI(Set<String> i)
    {
        I = i;
    }

    public void setBranch(Set<String> branch)
    {
        this.branch = branch;
    }

    public void setJ(Set<String> j)
    {
        this.J = j;
    }

    //��������
    public void run()
    {
        int RNum = (instructionNum - 6) / 3;//Rָ����Ŀ
        int Jnum = 2;//J�ͣ�jal��ָ����Ŀ
        int INum = instructionNum - 6 - RNum - Jnum;//Iָ����Ŀ
        int BranchNum = 5;

        //�����ǼĴ�����洢���ĳ�ʼ������
        this.initReg();

        //Rָ��
        for (int i = 0; i < RNum; i++)
        {
            try
            {
                addR();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }

        //Jָ�jal��
        for (int i = 0; i < Jnum; i++)
        {
            jInstruction = new Jal(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("jal");
            this.ans.add(("nop"));//�ӳٲ�
        }

        //Iָ�����ת�ģ�
        for (int i = 0; i < INum; i++)
        {
            try
            {
                addI();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }

        //Iָ���֧��ת�ģ�
        for (int i = 0; i < 2; i++)
        {
            try
            {
                addBranch();
                //�ӳٲ�
                addDelaySlot();
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
            cnt++;
        }

        if (this.J.contains("j"))
        {
            this.jInstruction = new J(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("j");
            //�ӳٲ�
            addDelaySlot();
        }

        //�������в��漰��ת��֧�Ķ�Ӧ���������ˣ���ͨ��end��ǩֱ��ָ�������ֹ��ͨ����
        this.ans.add("beq $0, $0, end");
        //�ӳٲ�
        addDelaySlot();

        //��������label����
        this.createLabel();

        //��֤�������н���
        this.ans.add("end:");
        FileGenerate fileGenerate = new FileGenerate(this.ans);
        fileGenerate.update();
    }

    private void addR() throws UnknownInstrException
    {
        int index;
        List<String> RList = new ArrayList<>(R);

        index = this.random.nextInt(RList.size());
        String instruction = RList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "addu":
                this.rInstruction = new Addu(this.writeProhibit, this.hasVal);
                break;
            case "subu":
                this.rInstruction = new Subu(this.writeProhibit, this.hasVal);
                break;
            case "or":
                this.rInstruction = new Or(this.writeProhibit, this.hasVal);
                break;
            case "sll":
                this.rInstruction = new Sll(this.writeProhibit, this.hasVal);
                break;
            case "sllv":
                this.rInstruction = new Sllv(this.writeProhibit, this.hasVal);
                break;
            case "slt":
                this.rInstruction = new Slt(this.writeProhibit, this.hasVal);
                break;
            case "jr":
            case "jalr":
                return;
            default:
                throw new UnknownInstrException(instruction);
        }

        newInstr = this.rInstruction.createMIPSText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
    }

    //���ӷǷ�֧��Iָ��
    private void addI() throws UnknownInstrException
    {
        int index;
        List<String> IList = new ArrayList<>(I);

        index = this.random.nextInt(IList.size());
        String instruction = IList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "lui":
                this.iInstruction = new Lui(this.writeProhibit, this.hasVal);
                break;
            case "ori":
                this.iInstruction = new Ori(this.writeProhibit, this.hasVal);
                break;
            case "lw":
                this.iInstruction = new Lw(this.addrList, this.writeProhibit, this.hasVal);
                break;
            case "lh":
                this.iInstruction = new Lh(this.addrList, this.writeProhibit, this.hasVal);
                break;
            case "lb":
                this.iInstruction = new Lb(this.addrList, this.writeProhibit, this.hasVal);
                break;
            case "sw":
                this.iInstruction = new Sw(this.hasVal, this.addrList);
                break;
            case "sh":
                this.iInstruction = new Sh(this.hasVal, this.addrList);
                break;
            case "sb":
                this.iInstruction = new Sb(this.hasVal, this.addrList);
                break;
            case "???":
                return;
            default:
                throw new UnknownInstrException(instruction);
        }

        newInstr = iInstruction.createMIPSText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
    }

    //�������Iָ���в�����֧��ָ��
    private void addBranch() throws UnknownInstrException
    {
        int index;
        List<String> branchList = new ArrayList<>(this.branch);

        index = this.random.nextInt(branchList.size());
        String instruction = branchList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "beq":
                this.iInstruction = new Beq(this.labelList);
                break;
            case "bne":
                this.iInstruction = new Bne(this.labelList);
                break;
            default:
                System.out.println("δָ֪��" + instruction);
                throw new UnknownInstrException(instruction);
        }

        newInstr = iInstruction.createMIPSText();
        if (newInstr != null)
        {
            ans.add(newInstr);
            this.labelList.add(instruction);
        }
    }

    //����ӳٲ�
    private void addDelaySlot()
    {
        boolean add = false;
        while (!add)
        {
            try
            {
                addR();
                add = true;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }
}

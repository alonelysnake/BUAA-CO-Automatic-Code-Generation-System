import Instruction.I.*;
import Instruction.J.J;
import Instruction.J.JInstruction;
import Instruction.J.Jal;
import Instruction.R.Addu;
import Instruction.R.Jr;
import Instruction.R.RInstruction;
import Instruction.R.Subu;

import java.io.*;
import java.util.*;

public class Generate
{
    private int instructionNum;//ָ������
    private int cnt = 0;//��ǰָ��index
    final private int labelBlockSize = 3;//label���С
    private Set<String> R;//ѡ���Rָ��
    private Set<String> I;//ѡ���Iָ�����֧��
    private Set<String> J;//ѡ���Jָ��
    private Set<String> branch;//I�еķ�ָ֧��

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
        this.ans.add("ori $v0, $0, 0xffff");
        //$v1($3)ֵ�̶�Ϊ-2^32
        this.ans.add("lui $v1, 0xffff");
        this.ans.add("ori $v1, $0, 0xffff");
        //��������Ĵ�����ֵ
        for (int i = 0; i < 3; i++)
        {
            iInstruction = new Ori(writeProhibit);
            this.ans.add(iInstruction.createMIPSText());
            //������ֵ�ļĴ���
            hasVal.add(iInstruction.getRt());
            this.cnt++;
        }
        //�洢����ֵ���������ȡָ����ԣ�
        for (int i = 0; i < 3; i++)
        {
            this.iInstruction = new Sw(this.hasVal);
            this.ans.add(iInstruction.createMIPSText());
            this.addrList.add(Integer.valueOf(iInstruction.getImm16()));
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
                Random random = new Random();
                boolean jalr = random.nextBoolean();
                if (this.R.contains("jalr") && jalr)
                {
                    //δʵ��
                    System.out.println("δʵ��");
                } else
                {
                    //���Կ���ori�������Ĵ���Ȼ��jr�Ǹ�
                    //ori
                    iInstruction = new Ori(this.writeProhibit, 31);
                    ans.add(iInstruction.createMIPSText());
                    //jr
                    rInstruction = new Jr(iInstruction.getRt());
                    ans.add(rInstruction.createMIPSText());
                }
            } else if (instr.equals("jalr"))
            {
                System.out.println("δʵ��");
            } else if (this.branch.contains(instr))
            {
                //beq��bne��bgez����
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
                ans.add("j end");
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
            boolean add = false;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            int mode = random.nextInt(2);
            int index;
            if (mode == 0)
            {
                //Rָ��
                List<String> RList = new ArrayList<>(R);
                while (!add)
                {
                    index = random.nextInt(RList.size());
                    add = addR(index, RList);
                }
            } else
            {
                //Iָ��
                List<String> Ilist = new ArrayList<>(I);
                while (!add)
                {
                    index = random.nextInt(Ilist.size());
                    add = addI(index, Ilist);
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
            boolean add = false;
            int index;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            List<String> RList = new ArrayList<>(R);

            while (!add)
            {
                index = random.nextInt(RList.size());
                add = addR(index, RList);
            }

            cnt++;
        }

        //Jָ�jal��
        for (int i = 0; i < Jnum; i++)
        {
            jInstruction = new Jal(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("jal");
        }

        //Iָ�����ת�ģ�
        for (int i = 0; i < INum; i++)
        {
            boolean add = false;
            int index;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            List<String> IList = new ArrayList<>(I);

            while (!add)
            {
                index = random.nextInt(IList.size());
                add = addI(index, IList);
            }

            cnt++;
        }

        //Iָ���֧��ת�ģ�
        for (int i = 0; i < BranchNum; i++)
        {
            boolean add = false;
            int index;
            int seed = new Random().nextInt();
            Random random = new Random(seed);
            List<String> branchList = new ArrayList<>(this.branch);

            while (!add)
            {
                index = random.nextInt(branchList.size());
                add = addBranch(index, branchList);
            }

            cnt++;
        }

        if (this.J.contains("j"))
        {
            this.jInstruction = new J(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("j");
        }

        //�������в��漰��ת��֧�Ķ�Ӧ���������ˣ���ͨ��end��ǩֱ��ָ�������ֹ��ͨ����
        this.ans.add("beq $0, $0, end");

        //��������label����
        this.createLabel();

        //��֤�������н���
        this.ans.add("end:");
        this.update(this.ans);
    }

    public boolean addR(int index, List<String> RList)
    {
        String instruction = RList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "addu":
                this.rInstruction = new Addu(this.writeProhibit, this.hasVal);
                newInstr = this.rInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(rInstruction.getRd());
                break;
            case "subu":
                this.rInstruction = new Subu(this.writeProhibit, this.hasVal);
                newInstr = this.rInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(rInstruction.getRd());
                break;
            case "jr":
            case "jalr":
                return false;
            default:
                System.out.println("δָ֪��" + instruction);
                return false;
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
        return true;
    }

    //���ӷǷ�֧��Iָ��
    public boolean addI(int index, List<String> IList)
    {
        String instruction = IList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "lui":
                this.iInstruction = new Lui(this.writeProhibit);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(iInstruction.getRt());
                break;
            case "ori":
                this.iInstruction = new Ori(this.writeProhibit);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(iInstruction.getRt());
                break;
            case "lw":
                this.iInstruction = new Lw(this.addrList, this.writeProhibit);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.hasVal.add(iInstruction.getRt());
                break;
            case "sw":
                this.iInstruction = new Sw(this.hasVal);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.addrList.add(Integer.valueOf(iInstruction.getImm16()));
                break;
            default:
                System.out.println("δָ֪��" + instruction);
                return false;
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
        return true;
    }

    //�������Iָ���в�����֧��ָ��
    public boolean addBranch(int index, List<String> branchList)
    {
        String instruction = branchList.get(index);
        String newInstr;
        switch (instruction)
        {
            case "beq":
                this.iInstruction = new Beq(instructionNum * 4, cnt * 4, this.labelList);
                break;
            case "bne":
                break;
            default:
                System.out.println("δָ֪��" + instruction);
                return false;
        }

        newInstr = iInstruction.createMIPSText();
        if (newInstr == null)
        {
            return false;
        }
        ans.add(newInstr);
        this.labelList.add(instruction);

        return true;
    }

    public void update(List<String> ans)
    {
        String fileName = "output.txt";
        try
        {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileName)));
            for (String text : ans)
            {
                bw.write(text);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e)
        {
            System.out.println("д��ʧ���ˡ�����");
        }
    }
}

import Instruction.I.*;
import Instruction.J.JInstruction;
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
    private Set<String> R;//ѡ���Rָ��
    private Set<String> I;//ѡ���Iָ��
    private Set<String> J;//ѡ���Jָ��

    RInstruction rInstruction;
    IInstruction iInstruction;
    JInstruction jInstruction;

    HashSet<Integer> writeProhibit = new HashSet<>();//������������д��ļĴ�����ϵͳ�ú�jr/jalr�õģ�
    HashSet<Integer> hasVal = new HashSet<>();//��ֵ�ļĴ���
    List<Integer> addrList = new ArrayList<>();
    List<String> labelList = new ArrayList<>();

    List<String> ans = new ArrayList<>();//������ַ�������

    public Generate(int instructionNum, Set<String> R, Set<String> I, Set<String> J)
    {
        this.setInstructionNum(instructionNum);
        this.setR(R);
        this.setI(I);
        this.setJ(J);
        this.initWriteProhibit();
    }

    private void initWriteProhibit()
    {
        writeProhibit.add(0);
        writeProhibit.add(1);
        writeProhibit.add(26);
        writeProhibit.add(27);
        writeProhibit.add(28);
        writeProhibit.add(29);
        writeProhibit.add(30);
        writeProhibit.add(31);
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

    public void setJ(Set<String> j)
    {
        J = j;
    }

    public void Run()
    {
        int RNum = (instructionNum - 6) / 3;//Rָ����Ŀ
        int INum = instructionNum - RNum - 6;//Iָ����Ŀ

        for (int i = 0; i < 3; i++)
        {
            iInstruction = new Ori(writeProhibit);
            ans.add(iInstruction.createMIPSText());
            //������ֵ�ļĴ���
            hasVal.add(iInstruction.getRt());

            cnt++;
        }
        for (int i = 0; i < 3; i++)
        {
            this.iInstruction = new Sw(this.hasVal);
            ans.add(iInstruction.createMIPSText());
            this.addrList.add(Integer.valueOf(iInstruction.getImm16()));

            cnt++;
        }
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

        //�������в��漰��ת��֧�Ķ�Ӧ���������ˣ���ͨ��end��ǩֱ��ָ�������ֹ��ͨ����
        ans.add("beq $0, $0, end");
        //���������תָ���֤ÿ��������ָ�ֻ��ռ����������beq��j����ori+beq��jal��ori+jr
        //��֤beq������ֱ�ӵ�end��jal���Ӧjr
        for(int i=0;i<this.labelList.size();i++)
        {
            //������label 'i' :
            String text="label"+i+":";
            ans.add(text);
            //��ͬ��֧��תָ�ͬ����
            String instr = this.labelList.get(i);
            switch (instr)
            {
                case "beq":
                    iInstruction=new Ori(this.writeProhibit);
                    ans.add(iInstruction.createMIPSText());
                    ans.add("beq $0, $0, end");
                    break;
                case "j":
                    break;
                case "jal":
                    break;
                case "jalr":
                    break;
                default:
                    System.out.println(" ��( �� �� ��|||)��  ���ڷ�֧��ת�����ֵ�ָ����");
                    System.out.println("����ָ�"+instr);
                    ans.add("beq $0, $0, end");
                    break;
            }
        }

        //��֤�������н���
        ans.add("end:");
        update(ans);
    }

    public boolean addR(int index, List<String> RList)
    {
        String instruction = RList.get(index);
        switch (instruction)
        {
            case "addu":
                this.rInstruction = new Addu(this.writeProhibit, this.hasVal);
                ans.add(this.rInstruction.createMIPSText());
                break;
            case "subu":
                this.rInstruction = new Subu(this.writeProhibit, this.hasVal);
                ans.add(this.rInstruction.createMIPSText());
                this.hasVal.add(this.rInstruction.getRd());
                break;
            case "jr":
                this.rInstruction = new Jr();
                ans.add(this.rInstruction.createMIPSText());
                break;
            case "jalr":
                break;
            default:
                System.out.println("δָ֪��" + instruction);
                return false;
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
        return true;
    }

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
            case "beq":
                this.iInstruction = new Beq(instructionNum * 4, cnt * 4, this.labelList);
                newInstr = iInstruction.createMIPSText();
                if (newInstr == null)
                {
                    return false;
                }
                ans.add(newInstr);
                this.labelList.add("beq");
                break;
            default:
                System.out.println("δָ֪��" + instruction);
                return false;
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
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

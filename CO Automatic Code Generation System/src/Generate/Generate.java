package Generate;

import Instruction.I.*;
import Instruction.I.IBranch.*;
import Instruction.I.ICal.*;
import Instruction.I.ILS.*;
import Instruction.J.*;
import Instruction.R.*;
import Exception.UnknownInstrException;

import java.util.*;

public class Generate
{
    private int instructionNum;//ָ������
    private int cnt = 0;//��ǰָ��index
    final private int labelBlockSize = 3;//label���С
    private Set<String> RCal;//ѡ���Rָ��
    private Set<String> ICal;//ѡ���Iָ�����֧��
    private Set<String> RJ;//jalr,jr
    private Set<String> LS;//load/saveָ��
    private Set<String> J;//ʵ��jump��ָ�j��jal��jalr��jr��
    private Set<String> branch;//I�еķ�ָ֧��
    private Set<String> MD;//�˳������

    final private int SEED = 114514;//��������������
    Random random = new Random(SEED);

    RInstruction rInstruction;
    IInstruction iInstruction;
    JInstruction jInstruction;

    Set<Integer> writeProhibit;//������������д��ļĴ�����ϵͳ�ú�jr/jalr�õģ�
    HashSet<Integer> hasVal = new HashSet<>();//��ֵ�ļĴ���
    List<Integer> addrList = new ArrayList<>();//��ֵ�Ĵ洢����ַ
    List<String> labelList = new ArrayList<>();//�洢��ָ֧��ĳ���˳��
    LinkedList<Integer> conflictReg = new LinkedList<>();//��¼�ᷢ����ͻ�ļĴ���

    List<String> ans = new ArrayList<>();//������ַ�������

    public Generate()
    {
        Init init = new Init();
        this.RCal = init.RCal;
        this.ICal = init.ICal;
        this.branch = init.branch;
        this.RJ = init.RJ;
        this.J = init.J;
        this.LS = init.LS;
        this.MD = init.MD;
        this.writeProhibit = init.writeProhibit;
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
            iInstruction = new Ori(writeProhibit, this.hasVal, this.conflictReg);
            this.ans.add(iInstruction.createMIPSText());
            //������ֵ�ļĴ���
            //hasVal.add(iInstruction.getRt());
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
                if (this.RCal.contains("jalr") && jalr)
                {
                    //δʵ��
                    System.out.println("δʵ��");
                } else
                {
                    //���Կ���ori�������Ĵ���Ȼ��jr�Ǹ�
                    //ori
                    iInstruction = new Ori(this.writeProhibit, this.conflictReg, 31, "0");
                    ans.add(iInstruction.getText());
                    //jr
                    rInstruction = new Jr(iInstruction.getRt());
                    ans.add(rInstruction.getText());
                }
                //�ӳٲ�
                createDelaySlot();
                //�ı���ʱ�����ַ����ֵ�ļĴ���Ϊ��д�루���뱣֤��������ӳٲ۲����õ�����Ĵ�����
                int rs = rInstruction.getRs();
                if (rs != 1 && rs != 31)
                {
                    this.writeProhibit.remove(rs);
                }
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
                            iInstruction = new Beq(label, this.conflictReg);
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
                createDelaySlot();
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
                    addRCal();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            } else
            {
                //Iָ��
                try
                {
                    addICal();
                } catch (UnknownInstrException e)
                {
                    e.printError();
                }
            }
        }
    }

    //����ӳٲ�
    private void createDelaySlot()
    {
        boolean add = false;
        while (!add)
        {
            try
            {
                addRCal();
                add = true;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    private void createMulDivBlock()
    {
        //����ʱ��֤�˳���֮ǰ�ȸ���������ֵ�������ǳ������벻Ϊ0������
        int[] cal = new int[2];
        for (int i = 0; i < 2; i++)
        {
            iInstruction = new Lui(writeProhibit, hasVal, conflictReg);
            this.ans.add(iInstruction.getText());
            int rs = iInstruction.getRs();
            String imm = Integer.toHexString(random.nextInt(65535));
            iInstruction = new Ori(writeProhibit, conflictReg, rs, imm);
            this.ans.add(iInstruction.getText());
            cal[i] = iInstruction.getRt();
        }
        try
        {
            //��ӳ˳�������
            addMD(cal[0], cal[1]);
            //�˳���
            for (int i = random.nextInt(5); i >= 0; i--)
            {
                createDelaySlot();
            }
            //��ͻָ��
            addMD(0, 0);
        } catch (UnknownInstrException e)
        {
            e.printError();
        }
    }

    public int getInstructionNum()
    {
        return instructionNum;
    }

    public Set<String> getRCal()
    {
        return RCal;
    }

    public Set<String> getICal()
    {
        return ICal;
    }

    public Set<String> getJ()
    {
        return J;
    }

    public void setInstructionNum(int instructionNum)
    {
        this.instructionNum = instructionNum;
    }

    public void setRCal(Set<String> RCal)
    {
        this.RCal = RCal;
    }

    public void setICal(Set<String> ICal)
    {
        this.ICal = ICal;
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
        int RNum = 10;//Rָ����Ŀ
        int JNum = 2;//J�ͣ�jal��ָ����Ŀ
        int INum = 15;//Iָ����Ŀ
        int LSNum = 10;
        int branchNum = 4;
        int RINum = 15;//RI���

        //�����ǼĴ�����洢���ĳ�ʼ������
        this.initReg();

        //Jָ�jal��
        testJ(JNum);
        //Rָ���ڲ�����
        testRCal(RNum);
        //Iָ�����ת�ģ��ڲ�����
        testICal(INum);
        //LS�ڲ�����
        testLS(LSNum);
        //�������RI����
        testRI(RINum);
        //Iָ���֧��ת�ģ�
        testBranch(branchNum);
        //����
        if (this.J.contains("j"))
        {
            this.jInstruction = new J(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("j");
            //�ӳٲ�
            createDelaySlot();
        }

        //�������в��漰��ת��֧�Ķ�Ӧ���������ˣ��ᱣ֤ͨ��end��ǩֱ��ָ�������ֹ
        this.ans.add("beq $0, $0, end");
        //�ӳٲ�
        createDelaySlot();

        //��������label����
        this.createLabel();

        //��֤�������н���
        this.ans.add("end:");
        FileGenerate fileGenerate = new FileGenerate(this.ans);
        fileGenerate.update();
    }

    private void addRCal() throws UnknownInstrException
    {
        String newInstr;

        this.rInstruction = InstrChoose.chooseRCal(RCal, this.writeProhibit, this.hasVal, this.conflictReg);
        newInstr = this.rInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
    }

    //���ӷǷ�֧��Iָ��
    private void addICal() throws UnknownInstrException
    {
        String newInstr;

        this.iInstruction = InstrChoose.chooseICal(ICal, writeProhibit, hasVal, conflictReg);
        newInstr = iInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
    }

    private void addLS() throws UnknownInstrException
    {

        String newInstr;

        iInstruction = InstrChoose.chooseLS(LS, addrList, writeProhibit, hasVal, conflictReg);
        newInstr = iInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
        //System.out.println("��� " + instruction + " ָ��ɹ�");
    }

    //�������Iָ���в�����֧��ָ��
    private void addBranch() throws UnknownInstrException
    {
        String newInstr;

        iInstruction = InstrChoose.chooseBranch(branch, labelList, conflictReg);
        newInstr = iInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
    }

    private void addMD(int rs, int rt) throws UnknownInstrException
    {
        String newInstr;

        rInstruction = InstrChoose.chooseMD(MD, rs, rt, writeProhibit, hasVal, conflictReg);
        newInstr = rInstruction.getText();
        if (newInstr != null)
        {
            ans.add(newInstr);
        }
    }

    void testRCal(int RNum)
    {
        for (int i = 0; i < RNum; i++)
        {
            try
            {
                addRCal();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    void testICal(int INum)
    {
        for (int i = 0; i < INum; i++)
        {
            try
            {
                addICal();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    void testLS(int LSNum)
    {
        for (int i = 0; i < LSNum; i++)
        {
            try
            {
                addLS();
                cnt++;
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
        }
    }

    void testMD(int MDNum)
    {
        for (int i = 0; i < MDNum; i++)
        {
            createMulDivBlock();
        }
    }

    void testJ(int JNum)
    {
        for (int i = 0; i < JNum; i++)
        {
            jInstruction = new Jal(this.labelList);
            this.ans.add(jInstruction.createMIPSText());
            this.labelList.add("jal");
            this.createDelaySlot();//�ӳٲ�
        }
    }

    void testRI(int RINum)
    {
        for (int i = 0; i < RINum; i++)
        {
            int num = random.nextInt(2);
            //���˳����Բ�ָͬ���������
            boolean[] block = {true, true, true, true};
            int cnt = 4;
            while (cnt > 0)
            {
                int index = random.nextInt(4);
                while (!block[index])
                {
                    index = random.nextInt(4);
                }
                switch (index)
                {
                    case 0:
                        testRCal(num);
                        break;
                    case 1:
                        testICal(num);
                        break;
                    case 2:
                        testLS(num);
                        break;
                    case 3:
                        testMD(num);
                        break;
                }
                block[index] = false;
                cnt--;
            }
        }
    }

    void testBranch(int branchNum)
    {
        for (int i = 0; i < branchNum; i++)
        {
            try
            {
                addBranch();
                //�ӳٲ�
                createDelaySlot();
            } catch (UnknownInstrException e)
            {
                e.printError();
            }
            cnt++;
        }
    }
}

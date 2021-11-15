import Instruction.InstructionDic;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class UI
{
    Set<String> R;
    Set<String> I;
    Set<String> branch;
    Set<String> J;
    int num;

    private Generate generate;

    public UI()
    {
        this.R = new HashSet<>();
        this.I = new HashSet<>();
        this.branch = new HashSet<>();
        this.J = new HashSet<>();
    }

    public void showInstruction()
    {
        System.out.println("Ŀǰ֧��ָ�");
        System.out.println("R:addu    subu    jr");
        System.out.println("I:ori    lui    sw    lw    beq");
        System.out.println("J:j(p4��Ҫ��������ʱ����)    jal");
    }

    public void chooseInstruction()
    {
        this.showInstruction();
        //�ز�ָ��
        R.add("addu");
        R.add("subu");
        I.add("ori");
        I.add("lui");
        I.add("sw");
        I.add("lw");
        branch.add("beq");

        Scanner scanner = new Scanner(System.in);
        System.out.println("�Ƿ�Ҫ�Զ������ָ���y/n����");
        String def = scanner.next();
        if (def.equals("y"))
        {
            //�Զ���
            System.out.println("����Ҫ���Ե�ָ�������7��ָ��Ϊ�ز�ָ������룬����endֹͣ����");
            String instr = scanner.next();
            while (!instr.equals("end"))
            {
                if (InstructionDic.RMODE.contains(instr))
                {
                    this.R.add(instr);
                    System.out.println("ָ��" + instr + "��ӳɹ�");
                } else if (InstructionDic.IMODE.contains(instr))
                {
                    this.I.add(instr);
                    System.out.println("ָ��" + instr + "��ӳɹ�");
                } else if (InstructionDic.JMODE.contains(instr))
                {
                    this.J.add(instr);
                    System.out.println("ָ��" + instr + "��ӳɹ�");
                } else if (InstructionDic.BRANCH.contains(instr))
                {
                    this.branch.add(instr);
                    System.out.println("ָ��" + instr + "��ӳɹ�");
                } else
                {
                    System.out.println(instr + "Ϊ��Ч��δʵ�֣�ָ����ʧ��");
                }
                System.out.print("��������Ҫ��ӵ�ָ�");
                instr = scanner.next();
            }
        } else
        {
            //Ĭ��
            //J.add("j");
            J.add("jal");
        }

        System.out.print("Ҫ���ɵ�ָ������Ϊ(������20-30��)��");
        this.num = scanner.nextInt();
    }

    public void run()
    {
        this.chooseInstruction();
        Generate generate = new Generate(num, R, I, branch, J);
        generate.run();
    }
}

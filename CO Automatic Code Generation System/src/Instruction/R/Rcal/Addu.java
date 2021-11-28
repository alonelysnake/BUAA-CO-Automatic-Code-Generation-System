package Instruction.R.Rcal;

import Instruction.InstructionDic;
import Instruction.RegDic;

import java.util.LinkedList;
import java.util.Set;

public class Addu extends RCalInstruction
{
    /*
     * ���췽��
     * ���룺
     * ��ֹ�޸ĵļĴ���������ָ����õģ�����at���ֺʹ�����תָ�����jr���ģ���Ĭ�ϣ�0����Ϊд��Ҳû�ã���1��26-31
     * ��ֵ�ļĴ��������ڸ��ֻ������㣩��0Ҳ���԰���������
     * */
    //Ĭ�Ϲ���
    public Addu(Set<Integer> writeProhibit, Set<Integer> hasVal, LinkedList<Integer> conflictReg)
    {
        super(writeProhibit, hasVal, conflictReg);
        this.setFunc(InstructionDic.ADDU);
    }

    @Override
    public String createMIPSText()
    {
        //addu rd, rs, rt
        return "addu $" + RegDic.RegName.get(this.getRd()) +
                ", $" +
                RegDic.RegName.get(this.getRs()) +
                ", $" +
                RegDic.RegName.get(this.getRt());
    }
}

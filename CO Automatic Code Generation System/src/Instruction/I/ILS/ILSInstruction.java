package Instruction.I.ILS;

import Instruction.I.IInstruction;

import java.util.List;

//load&save
abstract public class ILSInstruction extends IInstruction
{
    private List<Integer> addrList;//�Ѿ�д��ĵ�ַ

    public List<Integer> getAddrList()
    {
        return addrList;
    }

    public void setAddrList(List<Integer> addrList)
    {
        this.addrList = addrList;
    }

    //Ĭ�ϻ���ַΪ0
    @Override
    protected int chooseRs()
    {
        return 0;
    }

    @Override
    protected String chooseLabel()
    {
        return null;
    }

    @Override
    protected void setValue()
    {
        this.setRs(this.chooseRs());
        this.setRt(this.chooseRt());
        this.setImm16(this.chooseImm16());
        this.setText(this.createMIPSText());
    }
}

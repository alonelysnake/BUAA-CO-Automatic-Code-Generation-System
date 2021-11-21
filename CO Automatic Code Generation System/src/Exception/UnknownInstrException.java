package Exception;

public class UnknownInstrException extends Exception
{
    private final String instr;

    public UnknownInstrException(String instr)
    {
        this.instr = instr;
    }

    public void printError()
    {
        System.out.println("Î´ÖªÖ¸Áî" + instr);
    }
}

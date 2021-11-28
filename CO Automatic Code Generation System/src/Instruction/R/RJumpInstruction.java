package Instruction.R;

abstract public class RJumpInstruction extends RInstruction
{


    @Override
    protected int chooseRt()
    {
        return 0;
    }

    @Override
    protected int chooseShamt()
    {
        return 0;
    }
}

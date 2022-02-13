class OperationTypeException extends Exception
{
    private int i;

    public OperationTypeException(String msg)
    {
        super(msg);
    }

    public OperationTypeException(String msg, int x)
    {
        super(msg);
        i = x;
    }

    public int val()
    { 
        return i;
    }
}
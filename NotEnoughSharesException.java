class NotEnoughSharesException extends Exception
{
    private int i;

    public NotEnoughSharesException(String msg)
    {
        super(msg);
    }

    public NotEnoughSharesException(String msg, int x)
    {
        super(msg);
        i = x;
    }

    public int val()
    { 
        return i;
    }
}
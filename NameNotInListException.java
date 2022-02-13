class NameNotInListException extends Exception
{
    private int i;

    public NameNotInListException(String msg)
    {
        super(msg);
    }

    public NameNotInListException(String msg, int x)
    {
        super(msg);
        i = x;
    }

    public int val()
    { 
        return i;
    }
}
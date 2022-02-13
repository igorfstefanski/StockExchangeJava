public class Order
{
    private String typeOfOrder;
    private String shareName;
    private int numberOfShares;
    private int desiredPrice;
    private User owner;

    public Order(String type, String which, int number, int price, User user)
    {
        typeOfOrder = type;
        shareName = which;
        numberOfShares = number;
        desiredPrice = price;
        owner = user;
    }

    public String getTypeOfOrder()
    {
        return typeOfOrder;
    }

    public String getShareName()
    {
        return shareName;
    }

    public int getNumberOfShares()
    {
        return numberOfShares;
    }

    public int getDesiredPrice()
    {
        return desiredPrice;
    }

    public User getOwner()
    {
        return owner;
    }
}
import java.util.Random;
import java.lang.IllegalArgumentException;

public class Stock
{
    private String name;
    private Integer currentPrice;
    private Integer numberEmitted;

    public Stock(String companyName, int minPrice, int maxPrice)
    {
        name = companyName;
        try
        {
            setRandomNumberPrice(minPrice, maxPrice);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Wrong bounds for random!");
            e.printStackTrace();
        }
    }

    private void setRandomNumberPrice(int minPrice, int maxPrice) throws IllegalArgumentException
    {
        Random rand = new Random();

        numberEmitted = rand.nextInt(maxPrice+1);
        numberEmitted += minPrice;

        currentPrice = rand.nextInt(maxPrice+1);
        currentPrice += minPrice;
    }

    public String getName()
    {
        return name;
    }

    public int getCurrentPrice()
    {
        return currentPrice;
    }

    public int getNumberEmitted()
    {
        return numberEmitted;
    }

    public void setCurrentPrice(int newVal)
    {
        currentPrice = newVal;
    }

    public void setNumberEmitted(int newVal)
    {
        numberEmitted = newVal;
    }

    @Override
    public String toString()
    {
        return name + " => " + numberEmitted.toString() + " => " + currentPrice.toString();
    }
}
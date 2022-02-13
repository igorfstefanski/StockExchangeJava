import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Iterator;
import java.util.ListIterator;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.util.InputMismatchException;

public class ExchangeEngine
{
    private int kMinPrice = 1;
    private int kMaxPrice = 10000;
    private int kMinAssets = 5;
    private int kMaxAssets = 10;

    private List<Stock> allAssets = new ArrayList<Stock>();
    private List<Stock> currentAssets = new ArrayList<Stock>();
    private List<Order> waitingOrders = new ArrayList<Order>();

    public User currentUser;

    public void getAssetsFromFile(String filePath)
    {
        AssetsFileReader file = new AssetsFileReader("assets.txt", kMinPrice, kMaxPrice);
        allAssets = file.getAssetsRead();
    }

    public void askForCurrentUser()
    {
        Scanner scn = new Scanner(System.in);

        System.out.println("Username: ");
        String userName = scn.nextLine();
        currentUser = new User(userName);
    }

    public void chooseRandomAssets()
    {
        Random rand = new Random();

        int assetsNumber = (int)(Math.random() * (kMaxAssets - kMinAssets + 1) + kMinAssets);

        for(int i = 0; i < assetsNumber; i++)
        {
            currentAssets.add(allAssets.get(rand.nextInt(allAssets.size())));
        }
    }

    public void showCurrentStock()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println("\nFrom: " + dtf.format(now));
        System.out.println("Name/Number of shares emitted/Price(USD)");

        Iterator<Stock> iter = currentAssets.iterator();
        while(iter.hasNext())
        {
            System.out.println(iter.next());
        }
    }

    public void showUserShares()
    {
        for(Map.Entry<String, Integer> entry : currentUser.shares.entrySet())
        {
            System.out.println("Name: " + entry.getKey() + " Number: " + entry.getValue());
        }
    }

    public void placeOrder() throws InputMismatchException, OperationTypeException, NameNotInListException, NotEnoughSharesException
    {
        Scanner scn = new Scanner(System.in);

        System.out.println("Type of operation (BUY/SELL): ");
        String type = scn.nextLine();
        if(!type.equals("BUY") && !type.equals("SELL"))
        {
            throw new OperationTypeException("Wrong type of operation!");
        }
        System.out.println("Name of asset: ");
        String name = scn.nextLine();
        Stock tempStock = checkMarketForName(name);
        if(tempStock == null)
        {
            throw new NameNotInListException("The name does not exist!");
        }
        System.out.println("Number of shares: ");
        int number = scn.nextInt();
        if(type.equals("BUY") && number > tempStock.getNumberEmitted())
        {
            throw new NotEnoughSharesException("Not enough shares on the market!");
        }
        System.out.println("Desired price: ");
        int price = scn.nextInt();

        Order newOrder = new Order(type, name, number, price, currentUser);
        waitingOrders.add(newOrder);
    }

    private Stock checkMarketForName(String givenName)
    {
        ListIterator<Stock> iter = currentAssets.listIterator();
        while(iter.hasNext())
        {
            Stock tempStock = iter.next();

            if(tempStock.getName().equals(givenName))
            {
                return tempStock;
            }
        }
        return null;
    }

    public void updateExchange()
    {
        Random rand = new Random();
        int operation = rand.nextInt(2);
        int newVal = 0;
        final int randomPriceChange = (int)(Math.random() * 3 + kMinPrice);
        ListIterator<Stock> iter = currentAssets.listIterator();

        if(operation == 0)
        {
            while(iter.hasNext())
            {
                Stock tempStock = iter.next();
                newVal = tempStock.getCurrentPrice() - randomPriceChange;
                if(newVal < kMinPrice)
                {
                    newVal = kMinPrice;
                }
                tempStock.setCurrentPrice(newVal);
                iter.set(tempStock);
            }
        }
        else if(operation == 1)
        {
            while(iter.hasNext())
            {
                Stock tempStock = iter.next();
                newVal = tempStock.getCurrentPrice() + randomPriceChange;
                if(newVal > kMaxPrice)
                {
                    newVal = kMinPrice;
                }
                tempStock.setCurrentPrice(newVal);
                iter.set(tempStock);
            }
        }
    }

    public void performOrders()
    {
        String orderType = new String();
        Iterator<Order> itr = waitingOrders.iterator();

        while(itr.hasNext())
        {
            Order tempOrder = itr.next();
            orderType = tempOrder.getTypeOfOrder();
            if(orderType.equals("BUY"))
            {
                if(performBuy(tempOrder))
                {
                    itr.remove();
                }
            }
            else if(orderType.equals("SELL"))
            {
                if(performSell(tempOrder))
                {
                    itr.remove();
                }
            }
        }
    }

    private Boolean performBuy(Order order)
    {
        String shareName = order.getShareName();
        int desiredPrice = order.getDesiredPrice();
        int orderedNumberOfShares = order.getNumberOfShares();
        int newPrice;
        int newNumber;
        double percentage;
        ListIterator<Stock> iter = currentAssets.listIterator();

        while(iter.hasNext())
        {
            Stock tempStock = iter.next();

            if(tempStock.getName().equals(shareName))
            {
                if(tempStock.getCurrentPrice() <= desiredPrice)
                {
                    if(currentUser.shares.containsKey(shareName))
                    {
                        currentUser.shares.put(shareName, currentUser.shares.get(shareName) + orderedNumberOfShares);
                    }
                    else
                    {
                        currentUser.shares.put(shareName, orderedNumberOfShares);
                    }
                    newNumber = tempStock.getNumberEmitted() - orderedNumberOfShares;
                    percentage = ((double)orderedNumberOfShares / (double)newNumber) * 0.2;
                    newPrice = (int)(tempStock.getCurrentPrice() * (1 + percentage));

                    tempStock.setCurrentPrice(newPrice);
                    tempStock.setNumberEmitted(newNumber);
                    iter.set(tempStock);
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean performSell(Order order)
    {
        String shareName = order.getShareName();
        int desiredPrice = order.getDesiredPrice();
        int orderedNumberOfShares = order.getNumberOfShares();
        int newPrice;
        int newNumber;
        double percentage;
        ListIterator<Stock> iter = currentAssets.listIterator();

        while(iter.hasNext())
        {
            Stock tempStock = iter.next();

            if(tempStock.getName().equals(shareName))
            {
                if(tempStock.getCurrentPrice() >= desiredPrice)
                {
                    currentUser.shares.put(shareName, currentUser.shares.get(shareName) - orderedNumberOfShares);
                    newNumber = tempStock.getNumberEmitted() + orderedNumberOfShares;
                    percentage = ((double)orderedNumberOfShares / (double)newNumber) * 0.2;
                    newPrice = (int)(tempStock.getCurrentPrice() * (1 - percentage));

                    tempStock.setCurrentPrice(newPrice);
                    tempStock.setNumberEmitted(newNumber);
                    iter.set(tempStock);
                    return true;
                }
            }
        }
        return false;
    }
}
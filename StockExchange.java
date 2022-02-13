import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.InputMismatchException;

public class StockExchange
{
    public ExchangeEngine engine = new ExchangeEngine();
    public Timer engineTimer = new Timer();

    public static void main(String[] args)
    {
        StockExchange myStockExchange = new StockExchange();

        myStockExchange.engine.getAssetsFromFile("assets.txt");
        myStockExchange.engine.askForCurrentUser();
        myStockExchange.engine.chooseRandomAssets();

        TimerTask task = new TimerTask()
        {
            public void run()
            {
                myStockExchange.engine.updateExchange();
                myStockExchange.engine.performOrders();
            }
        };
        myStockExchange.engineTimer.scheduleAtFixedRate(task, 1000, 5000);

        myStockExchange.menu();
    }

    public void menu()
    {
        Scanner scn = new Scanner(System.in);
        Boolean isRunning = true;
        int operation;

        while(isRunning)
        {
            try
            {
                drawMenu();
                operation = scn.nextInt();
                switch(operation)
                {
                    case 0:
                        if(this.engine.currentUser.getName().equals("admin"))
                        {
                            stopTimer();
                        }
                        else
                        {
                            System.out.println("Wrong operation!");
                        }
                        break;
                    case 1:
                        this.engine.showCurrentStock();
                        break;
                    case 2:
                        this.engine.showUserShares();
                        break;
                    case 3:
                        this.engine.placeOrder();
                        break;
                    case 4:
                        stopTimer();
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Wrong operation!");
                        break;
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("Wrong operation!");
                scn.nextLine();
                continue;
            }
            catch(OperationTypeException e)
            {
                e.printStackTrace();
                continue;
            }
            catch(NameNotInListException e)
            {
                e.printStackTrace();
                continue;
            }
            catch(NotEnoughSharesException e)
            {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void drawMenu()
    {
        System.out.println("\n========");
        System.out.println("= MENU =");
        System.out.println("========");
        System.out.println("1. View current situation");
        System.out.println("2. View your shares");
        System.out.println("3. Place an order");
        System.out.println("4. Quit");
        if(this.engine.currentUser.getName().equals("admin"))
        {
            System.out.println("0. Stop stock engine");
        }
        System.out.println("Operation:");
    }

    private void stopTimer()
    {
        engineTimer.cancel();
        engineTimer.purge();
    }
}
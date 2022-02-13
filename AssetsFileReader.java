import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class AssetsFileReader
{
    private String filePath;
    private List<Stock> assets = new ArrayList<Stock>();

    public AssetsFileReader(String path, int minPrice, int maxPrice)
    {
        filePath = path;
        try
        {
            getAssetsFromFile(minPrice, maxPrice);
        }
        catch(IOException e)
        {
            System.out.println("Error reading the file!");
            e.printStackTrace();
        }
    }

    private void getAssetsFromFile(int minPrice, int maxPrice) throws IOException
    {
        BufferedReader br = null;
        File file = new File(filePath);
        br = new BufferedReader(new FileReader(file));
        String line = null;
        Stock newAsset;

        while((line = br.readLine()) != null )
        {
            if(!line.equals(""))
            {
                newAsset = new Stock(line, minPrice, maxPrice);
                assets.add(newAsset);
            }
        }
        if(br != null)
        {
            br.close();
        }
    }

    public List<Stock> getAssetsRead()
    {
        return assets;
    }
}
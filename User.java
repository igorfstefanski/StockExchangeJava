import java.util.Map;
import java.util.HashMap;

public class User
{
    private String userName;
    public Map<String, Integer> shares = new HashMap<String, Integer>();

    public User(String name)
    {
        userName = name;
    }

    public String getName()
    {
        return userName;
    }
}
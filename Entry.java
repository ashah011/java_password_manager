public class Entry 
{ 
    public final String website;
    public final String username;
    public final String password;
    public Entry(String website, String username, String password) 
    {
        this.website = website;
        this.username = username;
        this.password = password;
    }
    public String getWebsite() 
    {
        return website;
    }
    public String getUsername() 
    {
        return username;
    }
    public String getPassword() 
    {
        return password;
    }

    public String toString() 
    {
        return "Your Entry: Website: " + website + ", Username: " + username + ", Password: " + password;
    }
}

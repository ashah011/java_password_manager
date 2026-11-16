public class Entry 
{ 
    public final String website;
    public final String username;
    public final String password;
    
    public Entry(String website, String username, String password) 
    {
        if(website == null || website.isBlank())
        {
            throw new IllegalArgumentException("Website cannot be null or blank");
        }
        if(password == null || password.isBlank())
        {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        this.website = website;
        this.username = (username == null) ? "" : username;
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
        return "Entry[website=" + website + ", username=" + username + ", password=****]";
    }
}

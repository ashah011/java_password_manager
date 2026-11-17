public class Entry 
{ 
    private final String website; //values cannot be reassigned after construction, so these entries are immuteable and only this class can touch the fields directly
    private final String username;
    private final String password;
    
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
    @Override //replace the built-in toString method with a custom one that hides the password to prevent accidental exposure
    public String toString() 
    {
        return "Entry[website=" + website + ", username=" + username + ", password=****]";
    }
}

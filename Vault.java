import java.util.ArrayList;
//saves all the login information in a list of entry objects
public class Vault {
    private ArrayList<Entry> entries = new ArrayList<Entry>(); //list that holds entry objects

    public void addEntry(Entry entry)
    {
        if(entry == null)
        {
            throw new IllegalArgumentException("Entry cannot be null");
        }
        entries.add(entry);
    }
    public Entry getEntry(int index) //returns entry at specified index
    {
        try
        {
            return entries.get(index);
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }
    public void removeEntry(int index) //removes entry at specified index
    {
        try
        {
            entries.remove(index);
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }
    public int getSize() //makes sure the size of the list is correct
    {
        return entries.size(); 
    }
    public ArrayList<Entry> search(String query)
    {
        ArrayList<Entry> results = new ArrayList<>(); //new list for the matching entries
        String q = query.toLowerCase();
        for(Entry e: entries) //runs once per entry in the list and checks if the website contains the query string
        {
            if(e.getWebsite().toLowerCase().contains(q))
            {
                results.add(e);
            }
        }
        return results;
    }
    public ArrayList<Entry> getAll()
    {
        return new ArrayList<Entry>(entries);
    }
}
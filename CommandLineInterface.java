import java.util.List;
import java.util.Scanner;

public class CommandLineInterface 
{
    private final Vault vault = new Vault();
    private final Scanner scanner = new Scanner(System.in);
    public void run()
    {
        boolean isRunning = true;
        while(isRunning)
        {
            printMenu();
            String choice = scanner.nextLine();
            switch(choice)
            {
                case "1" -> addEntry();
                case "2" -> listEntries(vault.getAll());
                case "3" -> searchEntries();
                case "4" -> deleteEntry();
                case "5" -> isRunning = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye");
    }
    private void printMenu()
    {
        System.out.println("\n=== PASSWORD MANAGER ===");
        System.out.println("1. Add Entry");
        System.out.println("2. List Entries");
        System.out.println("3. Remove Entry");
        System.out.println("4. Delete Entry");
        System.out.println("5. Exit");
        System.out.print("> ");
    }
    private void addEntry()
    {
        System.out.print("site: ");
        String website = scanner.nextLine();
        System.out.print("username: ");
        String username = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();
        System.out.println("Notes (optional): ");
        String notes = scanner.nextLine();
        vault.addEntry(new Entry(website, username, password, notes));
        System.out.println("SAVED");
    }
    //this method lists all the entries in the vault, or a message if there are no entries
    private void listEntries(List<Entry> entries)
    {
        if(entries.isEmpty())
        {
            System.out.println("No Entries");
        }
        for(int i = 0; i < entries.size(); i++) //loop through the list of entries and print them out with their index
        {
            Entry e = entries.get(i);
            System.out.println((i + 1) + ". " + e.getWebsite() + " | " + e.getUsername() + " | " + e.getNotes());
        }
    }
    private void searchEntries()
    {
        System.out.print("Search: ");
        listEntries(vault.search(scanner.nextLine()));
    }
    private void deleteEntry()
    {
        listEntries(vault.getAll());
        System.out.println("Number to delete: ");
        try
        {
            int n = Integer.parseInt(scanner.nextLine().trim());
            System.out.println(vault.removeEntry(n-1) ? "Deleted" : "No entry found at that number");
        }
        catch(NumberFormatException e)
        {
            System.out.println("Please enter a valid number");
        }
    }
}

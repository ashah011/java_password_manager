   import javax.swing.*;
   import javax.swing.event.DocumentEvent;
   import javax.swing.event.DocumentListener;

   import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;

   public class Display extends JFrame
   {
      private final Vault vault;
      private final VaultStorage storage;
      private final char[] masterPassword;

      private final JTextField websiteField = new JTextField(20);
      private final JTextField usernameField = new JTextField(20);
      private final JPasswordField passwordField = new JPasswordField(20);
      private final JPasswordField confirmField = new JPasswordField(20);
      private final JProgressBar strengthBar = new JProgressBar(0, 100);
      private final JLabel statusLabel = new JLabel(" ");
      private final JButton saveButton = new JButton("SAVE ENTRY");
      private final JButton viewButton = new JButton("VIEW ENTRIES");

      public Display(Vault vault, VaultStorage storage, char[] master)
      {
         super("Password Manager");
         //creates the frame and sets the layout
         this.vault = vault;
         this.storage = storage;
         this.masterPassword = master;
         setDefaultCloseOperation(EXIT_ON_CLOSE);
         setLayout(new GridLayout(7, 2, 10, 10));
         add(new JLabel("Enter Website: "));
         add(new JLabel("Enter Username"));
         add(usernameField);
         add(new JLabel("Enter Password"));
         add(passwordField);
         add(new JLabel("Confirm Password"));
         add(confirmField);
         add(new JLabel("Password Strength"));
         add(strengthBar);
         add(statusLabel);
         add(saveButton);
         add(new JLabel(" "));
         add(viewButton);

         passwordField.getDocument().addDocumentListener(new DocumentListener()
         {
            @Override
            public void changedUpdate(DocumentEvent e)
            {
               updateStrengthBar();
            }
            @Override
            public void insertUpdate(DocumentEvent e)
            {
               updateStrengthBar();
            }
            @Override
            public void removeUpdate(DocumentEvent e)
            {
               updateStrengthBar();
            }
         });
         getRootPane().setDefaultButton(saveButton);
         saveButton.addActionListener(e -> handleSave());
         viewButton.addActionListener(e -> viewEntries());

         setSize(500,400);
         setLocationRelativeTo(null);
         setVisible(true);
      }

   private void handleSave()
   {
      char[] pass1 = passwordField.getPassword();
      char[] pass2 = confirmField.getPassword();
      try
      {
         if(!Arrays.equals(pass1, pass2))
         {
            statusLabel.setText("PASSWORDS DO NOT MATCH");
            return;
         }

         vault.addEntry(new Entry(websiteField.getText().trim(), 
                        usernameField.getText().trim(),
                        new String(pass1)));
         storage.save(vault, masterPassword);
         statusLabel.setText("SAVED! Numebr of Entries: " + vault.getSize());
         websiteField.setText("");
         usernameField.setText("");
      }
      catch(IllegalArgumentException ex) //blank website/pasword
      {
         statusLabel.setText(ex.getMessage());
      }
      catch(IOException | GeneralSecurityException ex)
      {
         vault.removeEntry(vault.getSize() - 1); //remove the last entry
         statusLabel.setText("Could not save entry: " + ex.getMessage() + "\nTRY AGAIN");
      }
      finally{
         Arrays.fill(pass1, '\0');
         Arrays.fill(pass2, '\0');
         passwordField.setText("");
         confirmField.setText("");
      }
   }
   private void viewEntries()
   {
      if(vault.getSize() == 0)
      {
         JOptionPane.showMessageDialog(this, "The vault is currently empty. Add an entry to continue with this action.");
         return;
      }

      ArrayList<Entry> all = vault.getAll();
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < all.size(); i++)
      {
         sb.append(i + 1).append(". ");
            .append(all.get(i).getWebsite());
            .append(" (").append(all.get(i).getUsername()).append(")\n");
      }
      sb.append("\nEnter a number to show its password");

      String input = JOptionPane.showInputDialog(this, sb.toString());
      if(input == null)
      {
         return; //cancel
      }
      try
      {
         Entry entry = vault.getEntry(Integer.parseInt(input.trim()) - 1);
         JOptionPane.showMessageDialog(this, entry.getWebsite() + "\nPassword: " + entry.getPassword());
      }
      catch(IllegalArgumentExeption ex)
      {
         JOptionPane.showMessageDialog(this, "No entry with that number");
      }
   }
   private void updateStrengthBar()
   {
      char[] pw = passwordField.getPassword();
      try
      {
         int score = StrengthChecker.score(pw);
         strengthBar.setValue(score);
         if(score < 50)
            setForeground(Color.RED);
         if(score == 50)
            setForeground(Color.ORANGE);
         if(score > 50)
            setForeground(Color.GREEN);
      }
      finally
      {
         Arrays.fill(pw, '\0');
      }
   }
}
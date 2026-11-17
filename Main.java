import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

public class Main
{
    public static void main(String[] args)
    {
      SwingUtilities.invokeLater(Main::start);
    }
    private static void start()
    {
      VaultStorage storage = new VaultStorage(Path.of("vault.dat")); //generic data file
      Vault vault = new Vault();
      char[] master = null;

      if(storage.vaultExists())
      {
        //UNLOCK: Up to 3 attempts.
        for(int attempt = 1; attempt <= 3 && master == null; attempt++)
        {
          char[] typed = askPassword("ENTER YOUR MASTER PASSWORD");
          if(typed == null)
            return;
          try
          {
            vault = storage.load(typed);
            master = typed;
          }
          catch(IOException | GeneralSecurityException e)
          {
            JOptionPane.showMessageDialog(null, e.getMessage());
            Arrays.fill(typed, '\0');
          }
        }
        if(master == null)
          return; //three wrong attempts
      }
      else
      {
        //FIRST RUN: create a master password
        while(master == null)
        {
          char[] pass1 = askPassword("Create a master password (at least "
                        + StrengthChecker.minLength + " characters):");
          if (pass1 == null)
            return;
          char[] pass2 = askPassword("Confirm your master password:");
          if (pass2 == null)
          {
            Arrays.fill(pass1, '\0');
            return;
          }
          String problem = StrengthChecker.validatePassword(pass1, pass2);
          Arrays.fill(pass2, '\0');
          if(problem != null)
          {
            JOptionPane.showMessageDialog(null,problem);
            Arrays.fill(pass1, '\0');
          }
          else
          {
            try
            {
              storage.save(vault, pass1);
              master = pass1;
            }
            catch(IOException | GeneralSecurityException e)
            {
              JOptionPane.showMessageDialog(null, "Could not create vault: " + e.getMessage());
              return;
            }
          }
        }
      }
      new Display(vault, storage, master);
    }
    private static char[] askPassword(String message)
    {
      JPasswordField field = new JPasswordField(20);
      int result = JOptionPane.showConfirmDialog(null, new Object[] { message, field },
              "Password Manager", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      return result == JOptionPane.OK_OPTION ? field.getPassword() : null;
    }
}
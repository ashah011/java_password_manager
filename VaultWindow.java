import javax.swing.*;

public class VaultWindow extends JFrame
{
    public VaultWindow(Vault vault, VaultStorage storage, char[] master)
    {
        super("Your Vault");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JLabel("Unlocked! Entries: " + vault.getSize()));
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
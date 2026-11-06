import javax.swing.JFrame;

public class Display extends javax.swing.JFrame
{
    public Display()
    {
       JFrame window = new JFrame("Password Manager");
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.setVisible(true);
       window.setSize(500,500);

    }
}

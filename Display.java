import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.util.Arrays;

public class Display extends JFrame
{
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);
    private final JTextField websiteField = new JTextField(20);
    private final JButton setButton = new JButton("Set your password!");
    private final JLabel statusLabel = new JLabel();
    private final JProgressBar strengthBar = new JProgressBar(0, 100);
   
    public Display()
    {
      //creates the frame and sets the layout
      super("Password Manager");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new GridLayout(5, 2, 10, 10));
      add(new JLabel("Enter Website"));
      add(websiteField);
      add(new JLabel("Enter Password"));
      add(passwordField);
      add(new JLabel("Confirm Password"));
      add(confirmField);
      add(new JLabel("Password Strength"));
      add(strengthBar);
      add(statusLabel);
      add(setButton);

      passwordField.getDocument().addDocumentListener(new DocumentListener()
      {
         public void insertUpdate(DocumentEvent e) {
            updateStrengthBar();
         }
         public void removeUpdate(DocumentEvent e) {
            updateStrengthBar();
         }
          public void changedUpdate(DocumentEvent e) {
            updateStrengthBar();
         }
      });
      getRootPane().setDefaultButton(setButton);
      setButton.addActionListener(e -> handlePassword());
      setButton.setOpaque(true);
         setSize(500,500);
         setLocationRelativeTo(null);
         setVisible(true);
   }

   
    private void handlePassword()
    {
      char[] pass1 = passwordField.getPassword();
      char[] pass2 = confirmField.getPassword();
      try
      {
         String problem = StrengthChecker.validatePassword(pass1, pass2);
         if(problem != null)
         {
            statusLabel.setText(problem);
         }
         else
         {
            statusLabel.setText("Password set successfully!");
            //use pass1 here to create an Entry object and store it in a list or database
         }
      }
      finally
      {
         Arrays.fill(pass1, '\0');            
         Arrays.fill(pass2, '\0');
         passwordField.setText("");
         confirmField.setText("");
      }
    }
   private void updateStrengthBar()
    {
      char[] pw = passwordField.getPassword();
      try
      {
         int score = StrengthChecker.score(pw);
         strengthBar.setValue(score);
         if(score < 40)
            strengthBar.setForeground(Color.RED);
         else if(score < 70)
            strengthBar.setForeground(Color.ORANGE);
         else
            strengthBar.setForeground(Color.GREEN);
      }
      finally
      {
         Arrays.fill(pw, '\0');
      }
    }
   
 }
import java.util.*;


public class StrengthChecker 
{
    public static final int minLength = 15;
    //returns null if password is valid, otherwise returns error message
    public static String validatePassword(char[] password, char[] confirmPassword) 
    {
        if (password.length < minLength) 
        {
            return "Password must be at least " + minLength + " characters long.";
        } 
         if (!Arrays.equals(password, confirmPassword)) 
        {
            return "Passwords do not match.";
        } 
        else 
        return null;

    }
    //returns 0-100 score of password strength based on length and characters
    public static int score(char[] password) 
    {
        int score = Math.min(password.length, minLength) * 4;   // length: up to 60
        boolean lower = false;
        boolean upper = false;
        boolean num = false;
        boolean special = false;
    for (char c : password)
    {
        if (Character.isLowerCase(c))      
            lower = true;
        else if (Character.isUpperCase(c)) 
            upper = true;
        else if (Character.isDigit(c))     
            num = true;
        else                               
            special = true;
    }
        if (lower)
            score += 10;
        if (upper)
            score += 10;
        if (num)
            score += 10;
        if (special)
            score += 10;
        return score;
    }
}
import org.mindrot.jbcrypt.BCrypt;
public class PasswordHasher 
{
    public String hash(char[] password)
    {
        return BCrypt.hashpw(new String(password), BCrypt.gensalt(12));
    }
    public boolean verify(char[] password, String hashedPassword)
    {
        return BCrypt.checkpw(new String(password), hashedPassword);
    }
}

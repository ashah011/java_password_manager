import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyDeriver 
{
     private static final int ITERATIONS = 600_000; //current OWASP recommendation, makes sure that the calculations are slow, making every wrong guess slow for an attacker
     private static final int KEY_LENGTH_BITS = 256;
     private static final int SALT_LENGTH_BYTES = 16;
     
     public static byte[] generateSalt() 
     {
        //creates an empty array of 16 bytes and fills it with random bytes using SecureRandom, which is a cryptographically secure random number generator
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
     }
     public SecretKey deriveKey(char[] password, byte[] salt) throws GeneralSecurityException
     { 
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH_BITS); //bundles the pasword, salt, iterations, and key legnth
        try
        {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); //runs the PBKDF2 algorithm and returns that key as bytes
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES"); //wraps the bytes as an AES key
        }
        finally
        {
            spec.clearPassword(); //wipes the password from memory for security reasons
        }
    }
}
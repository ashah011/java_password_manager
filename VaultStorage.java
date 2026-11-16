/*the purpose of this class is to save the vault to a file and load it back into the other classes
 *SAVING: turnes the entries into texts, encrypts them, and adds them to a file
 *LOADING: reads the file, checks the master password with bcrypt, derives the key, decrypts, and rebuilds the vault
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKey;

public class VaultStorage 
{
    private static final int HASH_LENGTH = 60;
    private static final int SALT_LENGTH = 16;
    private final Path file;
    private final PasswordHasher passwordHasher = new PasswordHasher();
    private final KeyDeriver keyDeriver = new KeyDeriver();
    private final EncryptionService encryption = new EncryptionService();

    //saves the vault to a file, encrypting it with the derived key and saving the salt and hashed master password
    public VaultStorage(Path file) 
    {
        this.file = file;
    }   
    //program knows to create a new vault or to unlock an existing one
    public boolean vaultExists() 
    {
        return Files.exists(file);
    }
    //Base64-encode each field to convert text into digits with no commas/newlines
    private String encode(String text)
    {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }
    private String decode(String encodedText)
    {
        return new String(Base64.getDecoder().decode(encodedText), StandardCharsets.UTF_8);
    }
    //each entry is saved as one line
    private byte[] toBytes(Vault vault)
    {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : vault.getAll()) //visits every entry
        {
            sb.append(encode(entry.getWebsite())).append(",")
              .append(encode(entry.getUsername())).append(",")
              .append(encode(entry.getPassword())).append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8); //converts the text to bytes
    }
    private Vault fromBytes(byte[] bytes) throws GeneralSecurityException
    {
        Vault vault = new Vault();
        String text = new String(bytes, StandardCharsets.UTF_8);
        for(String line : text.split("\n"))
        {
            if(line.isEmpty())
            {
                continue; //skip empty lines
            }
            String[] parts = line.split(",", - 1); //split into 3 parts, website, username, password
            if(parts.length != 3) //catch damaged files
            {
                throw new GeneralSecurityException("Invalid entry format"); //if the line doesn't have 3 parts, throw an exception
            }
            vault.addEntry(new Entry(decode(parts[0]), decode(parts[1]), decode(parts[2]))); //rebuild the entry and add it to the vault
        }
        return vault;
    }
    public void save(Vault vault, char[] masterPassword) throws IOException, GeneralSecurityException
    {
        byte[] hash = passwordHasher.hash(masterPassword).getBytes(StandardCharsets.UTF_8); //hash the master password with bcrypt
        byte[] salt = keyDeriver.generateSalt(); //generate a random salt for the key
        SecretKey key = keyDeriver.deriveKey(masterPassword, salt); //derive the key from the master password and salt
        byte[] encrypted = encryption.encrypt(toBytes(vault), key); //encrypt the vault with the derived key

        byte[] out = new byte[hash.length + salt.length + encrypted.length]; //combine the hash, salt, and encrypted vault into one array
        System.arraycopy(hash, 0, out, 0, hash.length); //copy the hash into the output array
        System.arraycopy(salt, 0, out, hash.length, salt.length); //copy the salt into the output array
        System.arraycopy(encrypted, 0, out, hash.length + salt.length, encrypted.length); //copy the encrypted vault into the output array

        Files.write(file, out); //write the output array to the file
    }
    public Vault load(char[] masterPassword) throws IOException, GeneralSecurityException
    {
        byte[] data = Files.readAllBytes(file);
        if(data.length < HASH_LENGTH + SALT_LENGTH) 
        {
            throw new GeneralSecurityException("Vault file is corrupted"); //if the file is too short to contain a hash and salt, throw an exception
        }
        String hash = new String(data, 0, HASH_LENGTH, StandardCharsets.UTF_8); //extract the hash from the file
        boolean passwordOk;
        try
        {
            passwordOk = passwordHasher.verify(masterPassword, hash); //verify the master password with bcrypt
        }
        catch(IllegalArgumentException e)
        {
            throw new GeneralSecurityException("Vault file is corrupted"); //if the hash is invalid, throw an exception
        }
        if(!passwordOk)
        {
            throw new GeneralSecurityException("Incorrect master password"); //if the password is incorrect, throw an exception
        }
        byte[] salt = Arrays.copyOfRange(data, HASH_LENGTH, HASH_LENGTH + SALT_LENGTH); //extract the salt from the file
        SecretKey key = keyDeriver.deriveKey(masterPassword, salt); //derive the key from the master password and salt
        byte[] encrypted = Arrays.copyOfRange(data, HASH_LENGTH + SALT_LENGTH, data.length); //extract the encrypted vault from the file
        return fromBytes(encryption.decrypt(encrypted, key)); //decrypt the vault and rebuild it
    }
}

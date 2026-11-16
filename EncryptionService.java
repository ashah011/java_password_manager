//uses AES-GCM (which scrambles the data and attaches a authentication tag to prove if the right key was used to encrypt the data) to encrypt and decrypt data

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class EncryptionService 
{
    private static final int IV_LENGTH_BYTES = 12; //initialization value, 12 random bytes that must be different for every encryption
    private static final int TAG_LENGTH_BITS = 128; //authentication tag length, 128 bits is the minimum recommended by NIST
    public byte[] encrypt(byte[] plaintext, SecretKey key) throws GeneralSecurityException
    {
        //make a random 12 byte IV
        byte[] iv = new byte[IV_LENGTH_BYTES];
        new SecureRandom().nextBytes(iv);
        //set up the cipher for AES-GCM encryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
        //encrypt the plaintext (result includes authentication tag))
        byte[] cipherText = cipher.doFinal(plaintext);
        //combine the IV and ciphertext into a single byte array
        byte[] result = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(cipherText, 0, result, iv.length, cipherText.length); //copies bytes from the ciphertext into the result array, starting at the end of the IV
        return result;
    }
    public byte[] decrypt(byte[] data, SecretKey key) throws GeneralSecurityException
    {
        if(data.length < IV_LENGTH_BYTES)
            throw new GeneralSecurityException("Data is too short to be valid");
        //split data into IV and ciphertext
        byte[] iv = Arrays.copyOfRange(data, 0, IV_LENGTH_BYTES); //copyof retuns a piece of an array
        byte[] cipherText = Arrays.copyOfRange(data, IV_LENGTH_BYTES, data.length);
        //set up cipher in decrypt mode with the same IV
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
        //decrypt, throws exception if key is wrong or if the data was tampered with
        return cipher.doFinal(cipherText);
    }
}

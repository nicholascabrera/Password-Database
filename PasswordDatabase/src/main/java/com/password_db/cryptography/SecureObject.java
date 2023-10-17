package com.password_db.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class SecureObject extends Password {
    private SecretKey key;
    private Cipher cyph;
    private byte[] iv;
    private IvParameterSpec IV;

    public SecureObject(){}

    public void init(){
        generateInitializationVector();
        generateKey();
    }

    public byte[] encrypt(String plaintext){
        try {
            this.cyph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            System.out.println("Salt: " + Base64.getEncoder().encodeToString(this.IV.getIV()));
            this.cyph.init(Cipher.ENCRYPT_MODE, this.key, this.IV);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        byte[] ciphertext = new byte[cyph.getOutputSize(plaintext.getBytes().length)];
        try {
            ciphertext = this.cyph.doFinal(plaintext.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        
        return ciphertext;
    }

    public String decrypt(byte[] ciphertext){
        try {
            this.cyph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            this.cyph.init(Cipher.DECRYPT_MODE, this.key, this.IV);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        byte[] plainBytes;
        String plaintext = "";
        try {
            plainBytes = this.cyph.doFinal(ciphertext);
            plaintext = new String(plainBytes, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        
        return plaintext;
    }

    public byte[] encrypt(String plaintext, byte[] salt){
        IvParameterSpec sal = new IvParameterSpec(salt);

        try {
            this.cyph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            this.cyph.init(Cipher.ENCRYPT_MODE, this.key, sal);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        byte[] ciphertext = new byte[cyph.getOutputSize(plaintext.getBytes().length)];
        try {
            ciphertext = this.cyph.doFinal(plaintext.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        
        return ciphertext;
    }

    public String decrypt(byte[] ciphertext, byte[] salt){
        IvParameterSpec sal = new IvParameterSpec(salt);

        try {
            this.cyph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            this.cyph.init(Cipher.DECRYPT_MODE, this.key, sal);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        byte[] plainBytes;
        String plaintext = "";
        try {
            plainBytes = this.cyph.doFinal(ciphertext);
            plaintext = new String(plainBytes, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        
        return plaintext;
    }

    public String decrypt(byte[] ciphertext, byte[] setKey, byte[] salt){
        IvParameterSpec sal = new IvParameterSpec(salt);
        SecretKey k = new SecretKeySpec(setKey, "AES");

        try {
            this.cyph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            this.cyph.init(Cipher.DECRYPT_MODE, k, sal);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        byte[] plainBytes;
        String plaintext = "";
        try {
            plainBytes = this.cyph.doFinal(ciphertext);
            plaintext = new String(plainBytes, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        
        return plaintext;
    }

    public byte[] encrypt(Password password) throws Exception{
        byte[] keyBytes = Arrays.copyOf(password.getPassword().getBytes(StandardCharsets.UTF_8), 24);
        SecretKey k = new SecretKeySpec(keyBytes, "DESede");

        // Your vector must be 8 bytes long
        String vector = "ABCD1234";
        IvParameterSpec intervalVector = new IvParameterSpec(vector.getBytes(StandardCharsets.UTF_8));

        // Make an encrypter
        Cipher encrypt = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, k, intervalVector);
        
        byte[] kBytes = this.getKey().getBytes(StandardCharsets.UTF_8);
        byte[] encryptedByted = encrypt.doFinal(kBytes);
        System.out.println(Base64.getEncoder().encodeToString(encryptedByted));
        return encryptedByted;
    }

    public byte[] encrypt(byte[] plainKey, Password password) throws Exception{
        byte[] keyBytes = Arrays.copyOf(password.getPassword().getBytes(StandardCharsets.UTF_8), 24);
        SecretKey k = new SecretKeySpec(keyBytes, "DESede");

        // Your vector must be 8 bytes long
        String vector = "ABCD1234";
        IvParameterSpec intervalVector = new IvParameterSpec(vector.getBytes(StandardCharsets.UTF_8));

        // Make an encrypter
        Cipher encrypt = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, k, intervalVector);
        
        byte[] encryptedByted = encrypt.doFinal(plainKey);
        System.out.println(Base64.getEncoder().encodeToString(encryptedByted));
        return encryptedByted;
    }

    public String decrypt(byte[] input, Password password)  throws Exception{
        byte[] keyBytes = Arrays.copyOf(password.getPassword().getBytes(StandardCharsets.UTF_8), 24);
        SecretKey k = new SecretKeySpec(keyBytes, "DESede");

        // Your vector must be 8 bytes long
        String vector = "ABCD1234";
        IvParameterSpec intervalVector = new IvParameterSpec(vector.getBytes(StandardCharsets.UTF_8));

        // Make a decrypter
        Cipher decrypt = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decrypt.init(Cipher.DECRYPT_MODE, k, intervalVector);

        byte[] decryptedBytes = decrypt.doFinal(input);
        System.out.println(Base64.getEncoder().encodeToString(decryptedBytes));
        return Base64.getEncoder().encodeToString(decryptedBytes);
    }

    public static byte[] generateSalt(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);
            
        return salt;
    }

    public String argonHash(Password passwordObject, byte[] salt){
        String password = passwordObject.getPassword();
            
        int iterations = 2,  memLimit = 66536, hashLength = 32, parallelism = 1;
            
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(iterations)
            .withMemoryAsKB(memLimit)
            .withParallelism(parallelism)
            .withSalt(salt);
            
        Argon2BytesGenerator generate = new Argon2BytesGenerator();
        generate.init(builder.build());

        byte[] result = new byte[hashLength];
        generate.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        

        Argon2BytesGenerator verifier = new Argon2BytesGenerator();
        verifier.init(builder.build());

        byte[] testHash = new byte[hashLength];
        verifier.generateBytes(password.getBytes(StandardCharsets.UTF_8), testHash, 0, testHash.length);

        if(Arrays.equals(result, testHash)){
            return Base64.getEncoder().encodeToString(result);
        }
        return "Unsuccessful";
    }

    public String argonHash(String text, byte[] salt){
            
        int iterations = 2,  memLimit = 66536, hashLength = 32, parallelism = 1;
            
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(iterations)
            .withMemoryAsKB(memLimit)
            .withParallelism(parallelism)
            .withSalt(salt);
            
        Argon2BytesGenerator generate = new Argon2BytesGenerator();
        generate.init(builder.build());

        byte[] result = new byte[hashLength];
        generate.generateBytes(text.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        

        Argon2BytesGenerator verifier = new Argon2BytesGenerator();
        verifier.init(builder.build());

        byte[] testHash = new byte[hashLength];
        verifier.generateBytes(text.getBytes(StandardCharsets.UTF_8), testHash, 0, testHash.length);

        if(Arrays.equals(result, testHash)){
            return Base64.getEncoder().encodeToString(result);
        }
        return "Unsuccessful";
    }

    public boolean verifier(Password passwordObject, String hash, String salt){
        String password = passwordObject.getPassword();
        byte[] NaCl = Base64.getDecoder().decode(salt);

        int iterations = 2,  memLimit = 66536, hashLength = 32, parallelism = 1;
            
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(iterations)
            .withMemoryAsKB(memLimit)
            .withParallelism(parallelism)
            .withSalt(NaCl);

        Argon2BytesGenerator verifier = new Argon2BytesGenerator();
        verifier.init(builder.build());

        byte[] testHash = new byte[hashLength];
        verifier.generateBytes(password.getBytes(StandardCharsets.UTF_8), testHash, 0, testHash.length);

        byte[] result = Base64.getDecoder().decode(hash);
        return Arrays.equals(testHash, result);
    }

    private void generateInitializationVector(){
        SecureRandom sr = new SecureRandom();                                           //random number generator, but secure
        this.iv = new byte[16];                                                         //byte array of size 2, since i only need 2 random numbers (case and letter)
        sr.nextBytes(this.iv);                                                          //store 2 random numbers into the byte array 'randoms'
        this.IV = new IvParameterSpec(this.iv);
    }

    private void generateKey(){
        KeyGenerator kGen;
        SecureRandom random = new SecureRandom();
        try {
            kGen = KeyGenerator.getInstance("AES");
            kGen.init(random);
            this.key = kGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getKey(){
        return Base64.getEncoder().encodeToString(this.key.getEncoded());
    }
}
package com.example.superpassman;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;


public class PassLogic {
    // Add instance variables for database connection and other necessary components
    private JsonDatabaseConnector databaseConnector;

    public PassLogic(JsonDatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public String generatePassword() {
        // Define the characters that can be used in the password
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digitChars = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?";

        // Combine all character sets into one
        String allChars = lowercaseChars + uppercaseChars + digitChars + specialChars;

        // Password length
        int passwordLength = 12;

        // Create a secure random number generator
        SecureRandom random = new SecureRandom();
        //Resource 1. “Java.Util.Random Class in Java.” GeeksforGeeks, GeeksforGeeks, 7 May 2019, www.geeksforgeeks.org/java-util-random-class-java/.

        // Initialize the password as an empty string
        StringBuilder password = new StringBuilder(passwordLength);

        // Generate random characters for the password
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(allChars.length());
            char randomChar = allChars.charAt(randomIndex);
            password.append(randomChar);
        }
        // Convert the StringBuilder to a String and return the generated password
        return password.toString();
    }

    public String encryptPassword(String passwordToEncrypt) {
        try {
            // Generate a random 16-byte initialization vector (IV)
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16];
            random.nextBytes(iv);

            String encryptionKey = Base64.getEncoder().encodeToString(iv); // Encode the IV to a base64-encoded string
            // Resource 2. baeldung, Written by: “Java Base64 Encoding and Decoding.” Baeldung, 28 Aug. 2023, www.baeldung.com/java-base64-encode-and-decode.
            SecretKey secretKey = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

            // Initialize the AES cipher in encryption mode with the IV and key
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

            // Encrypt the password
            byte[] encryptedBytes = cipher.doFinal(passwordToEncrypt.getBytes("UTF-8"));

            // Combine the IV and encrypted data into a single byte array
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            // Encode the combined byte array to a base64-encoded string
            String encryptedPassword = Base64.getEncoder().encodeToString(combined);

            return encryptedPassword;
        } catch (Exception e) {
            // Handle encryption-related exceptions
            e.printStackTrace();
            return null;
        }
    }

    public boolean storePassword(String website, String username, String password) {
        List<PasswordEntry> passwords = databaseConnector.getAllPasswords();
        PasswordEntry entry = new PasswordEntry(website, username, password);
        passwords.add(entry);
        return databaseConnector.savePasswords(passwords);
    }

    public List<PasswordEntry> searchPasswords(String searchQuery) {
        List<PasswordEntry> allPasswords = databaseConnector.getAllPasswords();
        List<PasswordEntry> searchResults = new ArrayList<>();
        for (PasswordEntry entry : allPasswords) {
            if (entry.getWebsite().contains(searchQuery)) {
                searchResults.add(entry);
            }
        }
        return searchResults;
    }

    public boolean deletePassword(String website) {
        List<PasswordEntry> passwords = databaseConnector.getAllPasswords();
        boolean removed = false;
        for (PasswordEntry entry : passwords) {
            if (entry.getWebsite().equals(website)) {
                passwords.remove(entry);
                removed = true;
                break;
            }
        }
        if (removed) {
            return databaseConnector.savePasswords(passwords);
        }
        return false;
    }
}
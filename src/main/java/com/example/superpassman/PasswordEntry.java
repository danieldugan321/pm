package com.example.superpassman;

//PasswordEntry is mainly here because it acts as the object to store the 3 values inserted into the database. Without password entry, inserting 3 values would indeed
//overwrite the previous entries.
public class PasswordEntry {
    private String website;
    private String username;
    private String encryptedPassword;

    public PasswordEntry(String website, String username, String encryptedPassword) {
        this.website = website;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    // Getter for website
    public String getWebsite() {
        return website;
    }

    // Setter for website
    public void setWebsite(String website) {
        this.website = website;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for encryptedPassword
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    // Setter for encryptedPassword
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public String toString() {
        return "PasswordEntry{" +
                "website='" + website + '\'' +
                ", username='" + username + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                '}';
    }
}

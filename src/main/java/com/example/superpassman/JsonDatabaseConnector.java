package com.example.superpassman;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonDatabaseConnector {
    private static final String JSON_FILE_PATH = "passwords.json";
    private ObjectMapper objectMapper;

    public JsonDatabaseConnector() {
        this.objectMapper = new ObjectMapper();
    }

    public List<PasswordEntry> getAllPasswords() {
        try {
            File jsonFile = new File(JSON_FILE_PATH);
            if (!jsonFile.exists()) {
                // If the JSON file doesn't exist, return an empty list
                return new ArrayList<>();
            }
            // Read the JSON file and deserialize its contents into a List<PasswordEntry>
            return objectMapper.readValue(jsonFile, new TypeReference<List<PasswordEntry>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean savePasswords(List<PasswordEntry> passwordEntries) {
        try {
            // Serialize the List<PasswordEntry> to JSON and write it to the JSON file
            objectMapper.writeValue(new File(JSON_FILE_PATH), passwordEntries);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

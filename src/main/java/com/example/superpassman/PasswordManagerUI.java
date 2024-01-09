package com.example.superpassman;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.List;
import java.util.Optional;

public class PasswordManagerUI extends Application {
    private PassLogic passLogic;


    public PasswordManagerUI(PassLogic passLogic) {
        this.passLogic = passLogic;
    }

    @Override
    public void start(Stage primaryStage) {
        TextArea passwordTextArea = new TextArea();
        passwordTextArea.setEditable(false);
        passwordTextArea.setPromptText("Generated or Encrypted Password will be displayed here");

        primaryStage.setTitle("Password Manager");

        //Add a title label to the UI
        Label titleLabel = new Label("Password Manager");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setAlignment(Pos.CENTER);
        // Add buttons to the UI
        Button generateButton = new Button("Generate Password");
        Button encryptButton = new Button("Encrypt Password");
        Button storeButton = new Button("Store Password");
        Button searchButton = new Button("Search Password");
        Button deleteButton = new Button("Delete Password");

        // Apply padding to the buttons
        generateButton.setPadding(new Insets(10, 20, 10, 20));
        encryptButton.setPadding(new Insets(10, 20, 10, 20));
        storeButton.setPadding(new Insets(10, 20, 10, 20));
        searchButton.setPadding(new Insets(10, 20, 10, 20));
        deleteButton.setPadding(new Insets(10, 20, 10, 20));

        passwordTextArea.setEditable(false);
        passwordTextArea.setPromptText("Generated or Encrypted Password will be displayed here");

        // Set up button actions
        generateButton.setOnAction(e -> {
            String generatedPassword = passLogic.generatePassword();
            showAlert("Generated Password", "Your generated password is:\n" + generatedPassword);
            copyToClipboard(generatedPassword);
            passwordTextArea.setText(generatedPassword);
        });
        // Set up button actions
        encryptButton.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Encrypt Password");
            inputDialog.setHeaderText(null);
            inputDialog.setContentText("Enter Password to Encrypt:");

            Optional<String> result = inputDialog.showAndWait();
            result.ifPresent(passwordToEncrypt -> {
                String encryptedPassword = passLogic.encryptPassword(passwordToEncrypt);
                showAlert("Encrypted Password", "Your encrypted password is:\n" + encryptedPassword);
                copyToClipboard(encryptedPassword);
            });
        });
        // Set up button actions
        storeButton.setOnAction(e -> {
            // Create input dialogs to collect website, username, and password from the user
            TextInputDialog websiteDialog = new TextInputDialog();
            websiteDialog.setTitle("Enter Website");
            websiteDialog.setHeaderText(null);
            websiteDialog.setContentText("Website:");

            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Enter Username");
            usernameDialog.setHeaderText(null);
            usernameDialog.setContentText("Username:");

            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Enter Password");
            passwordDialog.setHeaderText(null);
            passwordDialog.setContentText("Password:");

            // Show the input dialogs and collect user input
            Optional<String> websiteResult = websiteDialog.showAndWait();
            Optional<String> usernameResult = usernameDialog.showAndWait();
            Optional<String> passwordResult = passwordDialog.showAndWait();

            // Check if the user provided all necessary information
            if (websiteResult.isPresent() && usernameResult.isPresent() && passwordResult.isPresent()) {
                String website = websiteResult.get();
                String username = usernameResult.get();
                String password = passwordResult.get();

                // Call the storePassword method in PassLogic
                boolean success = passLogic.storePassword(website, username, password);

                if (success) {
                    showAlert("Password Stored", "Password successfully stored.");
                } else {
                    showAlert("Error", "Failed to store password. Please try again.");
                }
            } else {
                // User canceled one of the input dialogs, inform the user
                showAlert("Input Cancelled", "Password storage cancelled. Please provide all required information.");
            }
        });
        // Set up button actions
        searchButton.setOnAction(e -> {
            // Show a search dialog or form for the user to search for passwords
            TextInputDialog searchDialog = new TextInputDialog();
            searchDialog.setTitle("Search Passwords");
            searchDialog.setHeaderText(null);
            searchDialog.setContentText("Enter a search query:");

            Optional<String> result = searchDialog.showAndWait();
            result.ifPresent(searchQuery -> {
                List<PasswordEntry> searchResults = passLogic.searchPasswords(searchQuery);

                // Display search results to the user
                if (searchResults != null && !searchResults.isEmpty()) {
                    TableView<PasswordEntry> tableView = new TableView<>();
                    TableColumn<PasswordEntry, String> websiteColumn = new TableColumn<>("Website");
                    TableColumn<PasswordEntry, String> usernameColumn = new TableColumn<>("Username");
                    TableColumn<PasswordEntry, String> passwordColumn = new TableColumn<>("Encrypted Password");

                    // Define how to extract values from the PasswordEntry object
                    websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
                    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
                    passwordColumn.setCellValueFactory(new PropertyValueFactory<>("encryptedPassword"));

                    // Add columns to the TableView
                    tableView.getColumns().addAll(websiteColumn, usernameColumn, passwordColumn);

                    // Add search results to the TableView
                    tableView.getItems().addAll(searchResults);

                    // Create a new stage to display the TableView
                    Stage tableViewStage = new Stage();
                    tableViewStage.setTitle("Search Results");
                    Scene tableViewScene = new Scene(tableView, 600, 400);
                    tableViewStage.setScene(tableViewScene);
                    tableViewStage.show();
                } else {
                    showAlert("No Results", "No passwords matching the search query found.");
                }
            });
        });
        // Set up button actions
        deleteButton.setOnAction(e -> {
            // Show a dialog or form for the user to select a password to delete
            TextInputDialog deleteDialog = new TextInputDialog();
            deleteDialog.setTitle("Delete Password");
            deleteDialog.setHeaderText(null);
            deleteDialog.setContentText("Enter the website to delete:");

            Optional<String> result = deleteDialog.showAndWait();
            result.ifPresent(websiteToDelete -> {
                boolean success = passLogic.deletePassword(websiteToDelete);

                if (success) {
                    showAlert("Password Deleted", "Password for " + websiteToDelete + " successfully deleted.");
                } else {
                    showAlert("Error", "Failed to delete password. Please try again.");
                }
            });
        });
        // Set up the layout
        //Resource 3. “JavaFX: Vbox Class.” GeeksforGeeks, GeeksforGeeks, 6 Sept. 2018, www.geeksforgeeks.org/javafx-vbox-class/.
        VBox vbox = new VBox(10);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleLabel, generateButton, encryptButton, storeButton, searchButton, deleteButton);

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
    // Helper method to copy text to the clipboard
    // Resource 4. “JavaFX: Clipboard.” Clipboard (Javafx 8), 10 Feb. 2015, docs.oracle.com/javase/8/javafx/api/javafx/scene/input/Clipboard.html.
    private void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);

        showAlert("Text Copied", "Text copied to clipboard.");
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

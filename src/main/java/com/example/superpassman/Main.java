package com.example.superpassman;

import javafx.application.Application;
import javafx.stage.Stage;
//This program is a password manager. The buttons generate a password for the user, encrypt a password for the user, store a password for the user, search for a password for the user, and delete a password for the user. Enjoy!
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the database connection
        DatabaseConnector databaseConnector = new DatabaseConnector();

        // Initialize the PassLogic with the database connection
        PassLogic passLogic = new PassLogic(databaseConnector);

        // Start the PasswordManagerUI with the initialized PassLogic
        PasswordManagerUI passwordManagerUI = new PasswordManagerUI(passLogic);
        passwordManagerUI.start(primaryStage);
    }
}

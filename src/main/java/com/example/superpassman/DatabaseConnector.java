package com.example.superpassman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// This will access the database and read the data from it
public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pmconnection";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "confidante37";

    // This will connect to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // This will close the connection to the database
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}

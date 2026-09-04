package com.sylvie.clibank.api;

import java.sql.*;
import java.util.Scanner;


public class MainInterface {
    private static boolean quitApplication = false;
    public static void main(String[] args) {
        // Resizes the terminal window to 40 rows and 120 columns
        System.out.print("\033[8;40;120t");
        CLIFormatter formatter = new CLIFormatter();
        formatter.printWelcomeScreen();
        Scanner scanner = new Scanner(System.in);
        String host = System.getenv().get("DB_HOST");
        String port = System.getenv().get("DB_PORT");
        String dbName = System.getenv().get("DB_NAME");
        String user = System.getenv().get("DB_USER");
        String password = System.getenv().get("DB_PASSWORD");

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

        System.out.println("Connecting to database: " + url + " with: " + user + " & " + password);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Successfully connected to PostgreSQL!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        while (!quitApplication) {
            //main repl loop
            String input = scanner.nextLine();
        }

    }
}

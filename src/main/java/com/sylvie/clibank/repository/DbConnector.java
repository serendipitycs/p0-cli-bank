package com.sylvie.clibank.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    public static Connection establishConnection() {
        String host = System.getenv().get("DB_HOST");
        String port = System.getenv().get("DB_PORT");
        String dbName = System.getenv().get("DB_NAME");
        String user = System.getenv().get("DB_USER");
        String password = System.getenv().get("DB_PASSWORD");

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

        //System.out.println("Connecting to database: " + url + " with: " + user + " & " + password);

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Successfully connected to PostgreSQL!");
                return conn;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            //e.printStackTrace();
            return null;
        }
    }
}

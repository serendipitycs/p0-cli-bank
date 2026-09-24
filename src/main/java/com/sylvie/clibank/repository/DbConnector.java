package com.sylvie.clibank.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.DataBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    public static Connection establishConnection() {
        final Logger logger = LoggerFactory.getLogger(DbConnector.class);
        String host = System.getenv().get("DB_HOST");
        String port = System.getenv().get("DB_PORT");
        String dbName = System.getenv().get("DB_NAME");
        String user = System.getenv().get("DB_USER");
        String password = System.getenv().get("DB_PASSWORD");
        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (SQLException e) {
            logger.error("CRITICAL ERROR: DATABASE CONNECTION FAILED");
            return null;
        }
    }
}

package com.sylvie.clibank.repository;

import com.sylvie.clibank.repository.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {

    public static void createUser(Connection c, User user) {
        String sql = "INSERT INTO Users (account_num, pin, balance) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, user.getAccountNumber());
            ps.setInt(2, user.getAccountPin());
            ps.setDouble(3, user.getBalance());
            int rowsInserted = ps.executeUpdate();
            //logging?
        } catch (Exception e) {
            //logging
        }
    }

    public static User readUserByAccountNumber(Connection c, int accountNumber) {
        User returnUser = null;
        String sql = "SELECT * FROM Users WHERE account_num = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.getFetchSize() != 0) {
                returnUser.setAccountNumber(rs.getInt("account_num"));
                returnUser.setAccountPin(rs.getInt("pin"));
                returnUser.setBalance(rs.getDouble("balance"));
            }
        } catch (Exception e) {

        }
        return returnUser;
    }

    public static void updateUserBalanceByAccountNumber(Connection c, int accountNumber, double newBalance) {
        String sql = "UPDATE Users SET balance = ? WHERE account_num = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountNumber);
            int rowsInserted = ps.executeUpdate();
        } catch (Exception e) {

        }


    }
}


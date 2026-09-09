package com.sylvie.clibank.repository;

import com.sylvie.clibank.repository.models.User;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {
    private final Connection c;

    public UserRepository(Connection c) {
        this.c = c;
    }

    public void createUser(User user) {
        String sql = "INSERT INTO Users (account_num, pin, balance) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, user.getAccountNumber());
            ps.setString(2, user.getAccountPin());
            ps.setDouble(3, user.getBalance());
            int rowsInserted = ps.executeUpdate();
            //logging?
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public User readUserByAccountNumber(int accountNumber) {
        String sql = "SELECT * FROM Users WHERE account_num = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            int accountNum;
            String pin;
            double balance;
            if (rs.next()) {
                accountNum = rs.getInt("account_num");
                pin = rs.getString("pin");
                balance = rs.getDouble("balance");
                return new User(accountNumber,pin,balance);
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void updateUserBalanceByAccountNumber(int accountNumber, double newBalance) {
        String sql = "UPDATE Users SET balance = ? WHERE account_num = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountNumber);
            int rowsInserted = ps.executeUpdate();
        } catch (Exception e) {

        }

    }

    public int getNextAccountNumber() {
        String sql = "SELECT MAX(account_num) as current_acc_num FROM Users";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int max = (rs.getInt("current_acc_num")) + 1;
            if (rs.wasNull()) {
                return 100000;
            }
            System.out.println();
            return max;
        } catch (Exception e) {

        }
        return -1;
    }
}


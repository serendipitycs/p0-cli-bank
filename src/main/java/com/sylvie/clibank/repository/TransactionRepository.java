package com.sylvie.clibank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionRepository {
    private final Connection c;
    public TransactionRepository(Connection c) {this.c = c;}

    public void transfer(int fromAcc, int toAcc, double amount) {
        String withdrawSQL = "UPDATE Users SET balance = balance - ? WHERE account_num = ?;";
        String depositSQL = "UPDATE Users SET balance = balance + ? WHERE account_num = ?;";
        try (PreparedStatement psWithdraw = c.prepareStatement(withdrawSQL);
            PreparedStatement psDeposit = c.prepareStatement(depositSQL)){
            c.setAutoCommit(false);
            psWithdraw.setDouble(1,amount);
            psWithdraw.setInt(2,fromAcc);
            psDeposit.setDouble(1,amount);
            psDeposit.setInt(2,toAcc);
            psWithdraw.executeUpdate();
            psDeposit.executeUpdate();
            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try { c.rollback(); } catch (Exception ex) {}
        } finally {
             try { c.setAutoCommit(true); } catch (Exception e) {}
        }
    }

    public void addTransaction(int accountId, String type, double amount) {
        String sql = "INSERT INTO \"Transactions\" (account_id, type, amount, related_account_id) VALUES (?, ?, ?, NULL)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(int accountId, String type, double amount, int relAccountId) {
        String sql = "INSERT INTO \"Transactions\" (account_id, type, amount, related_account_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setInt(4,relAccountId);
            ps.executeUpdate();
        } catch (Exception e) {

        }
    }
}

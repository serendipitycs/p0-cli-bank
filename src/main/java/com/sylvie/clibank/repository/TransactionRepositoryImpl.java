package com.sylvie.clibank.repository;

import com.sylvie.clibank.repository.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Connection c;
    public TransactionRepositoryImpl(Connection c) {this.c = c;}

    @Override
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

    @Override
    public List<Transaction> getHistory(int accountId) {
        String sql = "SELECT * FROM \"Transactions\" WHERE account_id = ?";
        List<Transaction> returnList = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                    accountId,
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    rs.getInt("related_account_id"),
                    rs.getTimestamp("timestamp")
                );
                returnList.add(t);
            }
        } catch (Exception e) {

        }
        return returnList;
    }

    @Override
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

    @Override
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

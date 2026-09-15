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
}

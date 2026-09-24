package com.sylvie.clibank.repository;

import com.sylvie.clibank.repository.models.Transaction;

import java.util.List;

public interface TransactionRepository {

    void transfer(int fromAcc, int toAcc, double amount);

    List<Transaction> getHistory(int accountId);

    void addTransaction(int accountId, String type, double amount);

    void addTransaction(int accountId, String type, double amount, int relAccountId);
}

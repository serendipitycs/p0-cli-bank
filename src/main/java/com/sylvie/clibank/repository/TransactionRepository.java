package com.sylvie.clibank.repository;

import com.sylvie.clibank.repository.models.Transaction;

import java.util.List;

public interface TransactionRepository {

    public void transfer(int fromAcc, int toAcc, double amount);

    public List<Transaction> getHistory(int accountId);

    public void addTransaction(int accountId, String type, double amount);

    public void addTransaction(int accountId, String type, double amount, int relAccountId);
}

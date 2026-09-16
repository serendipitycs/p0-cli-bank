package com.sylvie.clibank.repository.models;

import java.sql.Timestamp;

public class Transaction {
    private int accountId;
    private String type;
    private double amount;
    private int relAccountId;
    private Timestamp timestamp;

    public Transaction(int accountId, String type, double amount, Timestamp timestamp) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        relAccountId = Integer.MIN_VALUE;
        this.timestamp = timestamp;
    }

    public Transaction(int accountId, String type, double amount, int relAccountId, Timestamp timestamp) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.relAccountId = relAccountId;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getRelAccountId() {
        return relAccountId;
    }

    public String getType() {
        return type;
    }

    public Timestamp getTimestamp() { return timestamp; }
}

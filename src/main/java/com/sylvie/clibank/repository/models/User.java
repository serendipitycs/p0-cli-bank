package com.sylvie.clibank.repository.models;

public class User {
    private int accountNumber;
    private String accountPin;
    private double balance;

    public User (int accountNumber, String accountPin)
    {
        this.accountNumber = accountNumber;
        this.accountPin = accountPin;
        balance = 0;
    }

    public User (int accountNumber, String accountPin, double balance)
    {
        this.accountNumber = accountNumber;
        this.accountPin = accountPin;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
    public String getAccountPin() {
        return accountPin;
    }
    public double getBalance() {
        return balance;
    }
}

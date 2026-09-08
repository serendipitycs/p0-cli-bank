package com.sylvie.clibank.repository.models;

public class User {
    private int accountNumber;
    private int accountPin;
    private double balance;

    public User (int accountNumber, int accountPin)
    {
        this.accountNumber = accountNumber;
        this.accountPin = accountPin;
        balance = 0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
    public int getAccountPin() {
        return accountPin;
    }
    public double getBalance() {
        return balance;
    }

}

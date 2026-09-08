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

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountPin(int accountPin) {
        this.accountPin = accountPin;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

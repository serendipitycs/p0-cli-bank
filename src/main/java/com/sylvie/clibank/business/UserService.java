package com.sylvie.clibank.business;

public interface UserService {

    public String deposit(int accountNum, double amount);

    public double getBalance(int accountNum);

    public String withdraw(int accountNum, double amount);
}
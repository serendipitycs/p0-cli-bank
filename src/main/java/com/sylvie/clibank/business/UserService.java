package com.sylvie.clibank.business;

public interface UserService {

    String deposit(int accountNum, double amount);

    double getBalance(int accountNum);

    String withdraw(int accountNum, double amount);
}
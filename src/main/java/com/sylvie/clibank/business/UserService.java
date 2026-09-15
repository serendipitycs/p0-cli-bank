package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;

public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean deposit(int accountNum, double amount) {
        if (amount < 0) return false;
        double newBalance = userRepo.readUserByAccountNumber(accountNum).getBalance() + amount;
        userRepo.updateUserBalanceByAccountNumber(accountNum, newBalance);
        return true;
    }

    public double getBalance(int accountNum) {
        return userRepo.getUserBalanceByAccountNumber(accountNum);
    }

    public String withdraw(int accountNum, double amount) {
        if (amount < 0) return "Negative";
        double newBalance = userRepo.readUserByAccountNumber(accountNum).getBalance() - amount;
        if (newBalance < 0) return "Insufficient";
        userRepo.updateUserBalanceByAccountNumber(accountNum, newBalance);
        return "Complete";
    }
}
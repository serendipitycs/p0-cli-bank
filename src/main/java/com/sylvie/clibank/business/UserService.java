package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;

public class UserService {
    private final UserRepository userRepo;
    private final TransactionService transServ;

    public UserService(UserRepository userRepo, TransactionService transServ) {
        this.userRepo = userRepo;
        this.transServ = transServ;
    }

    public boolean deposit(int accountNum, double amount) {
        if (amount < 0) return false;
        double newBalance = userRepo.readUserByAccountNumber(accountNum).getBalance() + amount;
        userRepo.updateUserBalanceByAccountNumber(accountNum, newBalance);
        transServ.addTransaction(accountNum,"Deposit", amount, -1);
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
        transServ.addTransaction(accountNum,"Withdraw", amount, -1);
        return "Complete";
    }

    public int getId(int accountNum) { return userRepo.getId(accountNum); }
}
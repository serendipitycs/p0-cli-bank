package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final TransactionService transServ;
    private final Logger logger;

    public UserServiceImpl(UserRepository userRepo, TransactionService transServ) {
        this.userRepo = userRepo;
        this.transServ = transServ;
        logger = LoggerFactory.getLogger(UserServiceImpl.class);
    }

    public String deposit(int accountNum, double amount) {
        if (amount < 0) {
            logger.error("Account # {} tried to deposit a negative amount!", accountNum);
            return "Failed";
        }
        double newBalance = userRepo.readUserByAccountNumber(accountNum).getBalance() + amount;
        userRepo.updateUserBalanceByAccountNumber(accountNum, newBalance);
        transServ.addTransaction(accountNum,"Deposit", amount, -1);
        logger.info("Account # {} Deposited: {}", accountNum, amount);
        return "Complete";
    }

    public double getBalance(int accountNum) {
        return userRepo.getUserBalanceByAccountNumber(accountNum);
    }

    public String withdraw(int accountNum, double amount) {
        if (amount < 0) {
            logger.error("Account # {} tried to withdraw a negative amount!", accountNum);
            return "Negative";
        }
        double newBalance = userRepo.readUserByAccountNumber(accountNum).getBalance() - amount;
        if (newBalance < 0) {
            logger.error("Account # {} tried to withdraw more than their balance!", accountNum);
            return "Insufficient";
        }
        userRepo.updateUserBalanceByAccountNumber(accountNum, newBalance);
        transServ.addTransaction(accountNum,"Withdraw", amount, -1);
        logger.info("Account # {} Withdrew: {}", accountNum, amount);
        return "Complete";
    }
}
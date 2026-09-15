package com.sylvie.clibank.business;

import com.sylvie.clibank.api.TransferState;
import com.sylvie.clibank.repository.TransactionRepository;
import com.sylvie.clibank.repository.UserRepository;

public class TransactionService {

    private final UserRepository userRepo;
    private final TransactionRepository transRepo;

    public TransactionService(UserRepository userRepo, TransactionRepository transRepo) {
        this.userRepo = userRepo;
        this.transRepo = transRepo;
    }

    public boolean accountNumExists(int accountNum) {
        return userRepo.readUserByAccountNumber(accountNum) != null;
    }

    //returns false if insufficient funds
    public TransferState transfer(int fromAcc, int toAcc, double amount) {
        if (amount < 0) return TransferState.F_INVALID;
        if ((userRepo.getUserBalanceByAccountNumber(fromAcc) - amount) < 0) return TransferState.F_INSUFFICIENT;
        transRepo.transfer(fromAcc,toAcc,amount);
        return TransferState.COMPLETE;
    }
}

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
        addTransaction(fromAcc,"TransferTo",amount,toAcc);
        addTransaction(toAcc,"TransferFrom",amount,fromAcc);
        return TransferState.COMPLETE;
    }

    public boolean addTransaction(int accountNum, String type, double amount, int relAccountNum) {
        boolean isTransfer = (relAccountNum != -1);
        if (userRepo.readUserByAccountNumber(accountNum) == null) return false;
        if (isTransfer && userRepo.readUserByAccountNumber(relAccountNum) == null) return false;
        if (amount < 0) return false;
        if (isTransfer) transRepo.addTransaction(userRepo.getId(accountNum), type, amount, userRepo.getId(relAccountNum));
        else transRepo.addTransaction(userRepo.getId(accountNum),type,amount);
        return true;
    }
}

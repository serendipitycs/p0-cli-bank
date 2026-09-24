package com.sylvie.clibank.business;

import com.sylvie.clibank.api.TransferState;
import com.sylvie.clibank.repository.TransactionRepository;
import com.sylvie.clibank.repository.UserRepository;
import com.sylvie.clibank.repository.models.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class TransactionService {

    private final UserRepository userRepo;
    private final TransactionRepository transRepo;
    private final Logger logger;

    public TransactionService(UserRepository userRepo, TransactionRepository transRepo) {
        this.userRepo = userRepo;
        this.transRepo = transRepo;
        logger = LoggerFactory.getLogger(TransactionService.class);
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
        logger.info("Account # {} transferred {} to Account # {}.", fromAcc, amount, toAcc);
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

    public List<Transaction> getHistory(int accountNum, int pageNum) {
        //0 or negative page numbers default to page 1
        if (pageNum < 1) pageNum = 1;
        List<Transaction> transactions = transRepo.getHistory(userRepo.getId(accountNum));
        //order transactions by my recent date
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
        int lowerIndex = (pageNum-1) * 4;
        //return empty list number too high
        if (lowerIndex >= transactions.size()) return List.of();
        int toIndex = Math.min(lowerIndex + 4, transactions.size());
        return transactions.subList(lowerIndex,toIndex);
    }

    public int getHistoryMaxPageNumber(int accountNum) {
        List<Transaction> transactions = transRepo.getHistory(userRepo.getId(accountNum));
        boolean hasRemainder = transactions.size() % 4 >= 1;
        return hasRemainder ? (transactions.size() / 4) + 1 : transactions.size() / 4;
    }
}

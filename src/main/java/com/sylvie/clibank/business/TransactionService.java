package com.sylvie.clibank.business;

import com.sylvie.clibank.api.TransferState;
import com.sylvie.clibank.repository.models.Transaction;
import java.util.List;

public interface TransactionService {
    public boolean accountNumExists(int accountNum);

    //returns false if insufficient funds
    public TransferState transfer(int fromAcc, int toAcc, double amount);

    public boolean addTransaction(int accountNum, String type, double amount, int relAccountNum);

    public List<Transaction> getHistory(int accountNum, int pageNum);

    public int getHistoryMaxPageNumber(int accountNum);
}

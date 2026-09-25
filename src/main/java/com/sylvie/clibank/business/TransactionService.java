package com.sylvie.clibank.business;

import com.sylvie.clibank.api.TransferState;
import com.sylvie.clibank.repository.models.Transaction;
import java.util.List;

public interface TransactionService {
    boolean accountNumExists(int accountNum);

    TransferState transfer(int fromAcc, int toAcc, double amount);

    boolean addTransaction(int accountNum, String type, double amount, int relAccountNum);

    List<Transaction> getHistory(int accountNum, int pageNum);

    int getHistoryCount(int accountNum);

    int getHistoryMaxPageNumber(int accountNum);
}

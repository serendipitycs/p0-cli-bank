package com.sylvie.clibank.repository;

import com.sylvie.clibank.repository.models.User;

public interface UserRepository {

    void createUser(User user);

    User readUserByAccountNumber(int accountNumber);

    void updateUserBalanceByAccountNumber(int accountNumber, double newBalance);

    double getUserBalanceByAccountNumber(int accountNumber);

    int getNextAccountNumber();

    int getId(int accountNumber);
}


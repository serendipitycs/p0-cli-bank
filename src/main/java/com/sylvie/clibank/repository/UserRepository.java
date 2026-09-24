package com.sylvie.clibank.repository;

import com.sylvie.clibank.repository.models.User;

public interface UserRepository {

    void createUser(User user);

    public User readUserByAccountNumber(int accountNumber);

    public void updateUserBalanceByAccountNumber(int accountNumber, double newBalance);

    public double getUserBalanceByAccountNumber(int accountNumber);

    public int getNextAccountNumber();

    public int getId(int accountNumber);
}


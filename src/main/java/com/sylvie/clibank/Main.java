package com.sylvie.clibank;

import com.sylvie.clibank.api.MainInterface;
import com.sylvie.clibank.business.*;
import com.sylvie.clibank.repository.*;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = DbConnector.establishConnection();
        //Repositories
        UserRepository userRepo = new UserRepositoryImpl(connection);
        TransactionRepository transRepo = new TransactionRepositoryImpl(connection);
        //Services
        AuthenticationService authServ = new AuthenticationServiceImpl(userRepo);
        TransactionService transServ = new TransactionServiceImpl(userRepo,transRepo);
        UserService userServ = new UserServiceImpl(userRepo,transServ);
        //API
        MainInterface mainInterface = new MainInterface(userServ,authServ,transServ);
        //Begin the application loop
        mainInterface.mainInterface();
    }
}

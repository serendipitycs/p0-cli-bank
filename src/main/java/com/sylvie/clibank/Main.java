package com.sylvie.clibank;

import com.sylvie.clibank.api.MainInterface;
import com.sylvie.clibank.business.AuthenticationService;
import com.sylvie.clibank.business.UserService;
import com.sylvie.clibank.repository.DbConnector;
import com.sylvie.clibank.repository.UserRepository;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = DbConnector.establishConnection();
        //Repositories
        UserRepository userRepo = new UserRepository(connection);
        //Services
        UserService userServ = new UserService(userRepo);
        AuthenticationService authServ = new AuthenticationService(userRepo);
        //API
        MainInterface mainInterface = new MainInterface(userServ,authServ);


        //Begin the application loop
        mainInterface.mainInterface();

    }
}

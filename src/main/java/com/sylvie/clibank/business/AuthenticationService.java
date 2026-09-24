package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;
import com.sylvie.clibank.repository.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class AuthenticationService {
    private final UserRepository userRepo;
    private final Logger logger;

    public AuthenticationService(UserRepository userRepo) {
        this.userRepo = userRepo;
        logger = LoggerFactory.getLogger(AuthenticationService.class);
    }

    private static boolean authenticatedUser = false;
    private static int authUserAccountNumber = -1;


    public boolean signIn (int accountNum, String pin) {
        User user = userRepo.readUserByAccountNumber(accountNum);
        if (user == null) {
            return false;
        }
        if (user.getAccountPin().equals(pin)) {
            authenticatedUser = true;
            authUserAccountNumber = accountNum;
            logger.info("Account # {} signed in.", authUserAccountNumber);
            return true;
        }
        return false;
    }

    public void signOut () {
        logger.info("Account # {} signed out.",authUserAccountNumber);
        authUserAccountNumber = -1;
        authenticatedUser = false;
    }

    public void register(String pin) {
        User user = new User(userRepo.getNextAccountNumber(),pin);
        userRepo.createUser(user);
        authenticatedUser = true;
        authUserAccountNumber = user.getAccountNumber();
        logger.info("Account # {} just registered.",authUserAccountNumber);
    }

    public boolean validateAccountNum(int accNum) {
        return userRepo.readUserByAccountNumber(accNum) != null;
    }

    public boolean validatePIN(String pin) {
        try {
            int pinInt = Integer.parseInt(pin);
            if (pinInt < 0) return false;
            if (pin.length() > 4) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAuthenticatedUser() {
        return authenticatedUser;
    }

    public int getAuthUserAccountNumber() {
        return authUserAccountNumber;
    }
}

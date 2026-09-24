package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;
import com.sylvie.clibank.repository.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepo;
    private final Logger logger;

    public AuthenticationServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
        logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    }

    private static boolean authenticatedUser = false;
    private static int authUserAccountNumber = -1;

    @Override
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

    @Override
    public void signOut () {
        logger.info("Account # {} signed out.",authUserAccountNumber);
        authUserAccountNumber = -1;
        authenticatedUser = false;
    }

    @Override
    public void register(String pin) {
        User user = new User(userRepo.getNextAccountNumber(),pin);
        userRepo.createUser(user);
        authenticatedUser = true;
        authUserAccountNumber = user.getAccountNumber();
        logger.info("Account # {} just registered.",authUserAccountNumber);
    }

    @Override
    public boolean validateAccountNum(int accNum) {
        return userRepo.readUserByAccountNumber(accNum) != null;
    }

    @Override
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

    @Override
    public boolean isAuthenticatedUser() {
        return authenticatedUser;
    }

    @Override
    public int getAuthUserAccountNumber() {
        return authUserAccountNumber;
    }
}

package com.sylvie.clibank.business;

import com.sylvie.clibank.repository.UserRepository;
import com.sylvie.clibank.repository.models.User;

import java.sql.Connection;

public class AuthenticationService {
    private final UserRepository userRepo;

    public AuthenticationService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private static boolean authenticatedUser = false;
    private static int authUserAccountNumber = -1;


    public boolean signIn (int accountNum, String pin) {
        User user = userRepo.readUserByAccountNumber(accountNum);
        if (user == null) {
            return false;
        }
        if (user.getAccountNumber() == accountNum && user.getAccountPin().equals(pin)) {
            authenticatedUser = true;
            authUserAccountNumber = accountNum;
            return true;
        }
        return false;
    }

    public void signOut () {
        authUserAccountNumber = -1;
        authenticatedUser = false;
    }

    public void register(String pin) {
        User user = new User(userRepo.getNextAccountNumber(),pin);
        userRepo.createUser(user);
        authenticatedUser = true;
        authUserAccountNumber = user.getAccountNumber();
    }

    public boolean validatePIN(String pin) {
        try {
            int pinInt = Integer.parseInt(pin);
            if (pinInt < 0) return false;
            if (pinInt > 9999) return false;
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

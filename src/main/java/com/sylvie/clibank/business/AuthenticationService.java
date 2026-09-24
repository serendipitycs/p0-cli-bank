package com.sylvie.clibank.business;

public interface AuthenticationService {
    boolean signIn(int accountNum, String pin);

    void signOut ();

    void register(String pin);

    boolean validateAccountNum(int accNum);

    boolean validatePIN(String pin);

    boolean isAuthenticatedUser();

    int getAuthUserAccountNumber();
}

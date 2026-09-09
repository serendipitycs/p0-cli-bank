package com.sylvie.clibank.api;

import com.sylvie.clibank.business.AuthenticationService;
import com.sylvie.clibank.business.UserService;

import java.sql.*;
import java.util.Scanner;


public class MainInterface {
    private final UserService userServ;
    private final AuthenticationService authServ;

    public MainInterface(UserService userServ, AuthenticationService authServ) {
        this.userServ = userServ;
        this.authServ = authServ;
    }

    private static boolean quitApplication = false;

    public void mainInterface() {
        // Resizes the terminal window to 40 rows and 120 columns
        System.out.print("\033[8;40;120t");
        CLIFormatter formatter = new CLIFormatter(userServ);
        formatter.printWelcomeScreen(authServ.getAuthUserAccountNumber());
        Scanner scanner = new Scanner(System.in);

        while (!quitApplication) {
            //main repl loop
            String input = scanner.nextLine();

            switch (input) {
                case "help":
                    if (authServ.isAuthenticatedUser()) {
                        formatter.printAuthedHelpScreen();
                    }
                    else {
                        formatter.printNoAuthHelpScreen();
                    }
                    break;
                case "login":
                    boolean signedIn = false;
                    boolean failedSignIn = false;
                    while (!signedIn){
                        if (!failedSignIn) {
                            formatter.printLoginScreen1();
                        } else {
                            formatter.printLoginScreenFail();
                        }
                        int accountNum = -1;
                        try {
                            accountNum = scanner.nextInt();
                        } catch (Exception e) {}
                        formatter.printLoginScreen2();
                        String pin = scanner.next();
                        if (authServ.signIn(accountNum,pin)) {
                            formatter.printLoginSuccessfulScreen(accountNum);
                            signedIn = true;
                        } else {
                            failedSignIn = true;
                        }
                    }
                    break;
                case "logout":
                    authServ.signOut();
                    formatter.printLogoutScreen();
                    break;
                case "register":
                    formatter.printRegisterScreen(false);
                    boolean validPin = false;
                    String pin = "";
                    while (!validPin) {
                        pin = scanner.next();
                        if (!authServ.validatePIN(pin)) {
                            validPin = false;
                            formatter.printRegisterScreen(true);
                        } else {
                            validPin = true;
                        }
                    }
                    authServ.register(pin);
                    formatter.printSuccessfulRegistrationScreen(authServ.getAuthUserAccountNumber());
                    break;
                case "history":
                    break;
                case "logs":
                    break;
                case "withdraw":
                    break;
                case "deposit":
                    break;
                case "transfer":
                    break;
                default:
                    break;
            }
        }

    }
}

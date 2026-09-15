package com.sylvie.clibank.api;

import com.sylvie.clibank.business.AuthenticationService;
import com.sylvie.clibank.business.UserService;

import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;


public class MainInterface {
    private final UserService userServ;
    private final AuthenticationService authServ;
    private final CLIFormatter formatter;
    private final Scanner scanner;

    public MainInterface(UserService userServ, AuthenticationService authServ) {
        this.userServ = userServ;
        this.authServ = authServ;
        formatter = new CLIFormatter(userServ,authServ);
        scanner = new Scanner(System.in);
    }

    private static boolean quitApplication = false;

    @SuppressWarnings("unchecked")
    public void mainInterface() {
        // Resizes the terminal window to 40 rows and 120 columns
        System.out.print("\033[8;40;120t");
        formatter.printToScreen("welcome",null);

        while (!quitApplication) {
            //main repl loop
            String input = scanner.nextLine();

            switch (input) {
                case "help":
                    if (authServ.isAuthenticatedUser()) formatter.printToScreen("helpAuth",null);
                    else formatter.printToScreen("helpNoAuth",null);
                    break;
                case "help 2":
                    if (authServ.isAuthenticatedUser()) formatter.printToScreen("helpAuth2",null);
                    else formatter.printToScreen("helpNoAuth",null);
                    break;
                case "login":
                    login();
                    break;
                case "logout":
                    logout();
                    break;
                case "register":
                    register();
                    break;
                case "history":
                    break;
                case "logs":
                    break;
                case "withdraw":
                    if (!authServ.isAuthenticatedUser()) continue;
                    withdraw();
                    break;
                case "deposit":
                    if (!authServ.isAuthenticatedUser()) continue;
                    deposit();
                    break;
                case "transfer":
                    break;
                default:
                    break;
            }
        }
    }

    private void register() {
        formatter.printToScreen("register",null);
        boolean validPin = false;
        String pin = "";
        while (!validPin) {
            pin = scanner.next();
            if (!authServ.validatePIN(pin)) {
                validPin = false;
                formatter.printToScreen("registerFailed",null);
            } else {
                validPin = true;
            }
        }
        authServ.register(pin);
        formatter.printToScreen("registerSuccess",pin);
    }

    private void login() {
        boolean signedIn = false;
        boolean failedSignIn = false;
        while (!signedIn){
            if (!failedSignIn) {
                formatter.printToScreen("login",null);
            } else {
                formatter.printToScreen("loginFailed",null);
            }
            int accountNum = -1;
            try {
                accountNum = scanner.nextInt();
            } catch (Exception e) {}
            formatter.printToScreen("login2",null);
            String pin = scanner.next();
            if (authServ.signIn(accountNum,pin)) {
                formatter.printToScreen("loginSuccess",null);
                signedIn = true;
            } else {
                failedSignIn = true;
            }
        }
    }

    private void logout() {
        authServ.signOut();
        formatter.printToScreen("logout",null);
    }

    private void withdraw() {
        String withdrawd = "Init";
        int accountNum = authServ.getAuthUserAccountNumber();
        double withdrawAmt = 0;
        formatter.printToScreen("withdraw",null);
        while (!withdrawd.equals("Complete")) {
            if (withdrawd.equals("Negative")) formatter.printToScreen("withdrawFailed",null);
            if (withdrawd.equals("Insufficient")) formatter.printToScreen("withdrawInsufficient",null);
            try {
                withdrawAmt = scanner.nextDouble();
                withdrawd = userServ.withdraw(accountNum,withdrawAmt);
            } catch (Exception e) {}
        }
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.printToScreen("withdrawSuccess",usFormat.format(withdrawAmt));
    }

    private void deposit() {
        boolean deposited = false;
        boolean failed = false;
        int accountNum = authServ.getAuthUserAccountNumber();
        double depositAmt = 0;
        formatter.printToScreen("deposit",null);
        while (!deposited) {
            if (failed) formatter.printToScreen("depositFailed",null);
            try {
                depositAmt = scanner.nextDouble();
                deposited = userServ.deposit(accountNum,depositAmt);
                if (!deposited) failed = true;
            } catch (Exception e) {}
        }
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.printToScreen("depositSuccess", usFormat.format(depositAmt));
    }
}

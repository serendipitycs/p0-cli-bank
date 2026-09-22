package com.sylvie.clibank.api;

import com.sylvie.clibank.business.AuthenticationService;
import com.sylvie.clibank.business.TransactionService;
import com.sylvie.clibank.business.UserService;
import com.sylvie.clibank.repository.models.Transaction;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class MainInterface {
    private final UserService userServ;
    private final AuthenticationService authServ;
    private final TransactionService transServ;
    private final CLIFormatter formatter;
    private final Scanner scanner;

    public MainInterface(UserService userServ, AuthenticationService authServ, TransactionService transServ) {
        this.userServ = userServ;
        this.authServ = authServ;
        this.transServ = transServ;
        formatter = new CLIFormatter(userServ,authServ);
        scanner = new Scanner(System.in);
    }

    private static boolean quitApplication = false;

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
                    boolean cancelled = login();
                    if (cancelled) formatter.printToScreen("welcome",null);
                    break;
                case "logout":
                    if (!authServ.isAuthenticatedUser()) {
                        continue;
                    }
                    logout();
                    break;
                case "register":
                    register();
                    break;
                case "history":
                    if (!authServ.isAuthenticatedUser()) {
                        commandRestricted(input);
                        continue;
                    }
                    history(1);
                    break;
                case "logs":
                    break;
                case "withdraw":
                    if (!authServ.isAuthenticatedUser()) {
                        commandRestricted(input);
                        continue;
                    }
                    withdraw();
                    break;
                case "deposit":
                    if (!authServ.isAuthenticatedUser()) {
                        commandRestricted(input);
                        continue;
                    }
                    deposit();
                    break;
                case "transfer":
                    if (!authServ.isAuthenticatedUser()) {
                        commandRestricted(input);
                        continue;
                    }
                    transfer();
                    break;
                case "quit":
                    quitApplication = true;
                default:
                    //regex matching any # of history page
                    if (input.matches("history \\d+")) {
                        history(Integer.parseInt(input.substring(8)));
                    }
                    else {
                        unknownCommand(input);
                    }
                    break;
            }
        }
    }

    private void commandRestricted(String input) {
        formatter.printToScreen("commandRestricted",input.substring(0, Math.min(input.length(), 20)));
    }

    private void unknownCommand(String input) {
        formatter.printToScreen("unknownCommand",input.substring(0, Math.min(input.length(), 20)));
    }

    private void register() {
        formatter.printToScreen("register",null);
        boolean validPin = false;
        String pin = "";
        while (!validPin) {
            pin = scanner.nextLine();
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

    private boolean login() {
        LoginState state = LoginState.INIT;
        int accountNum = -1;
        String pin;
        formatter.printToScreen("login",null);
        //stage 1 collect & validate account #
        while (state != LoginState.IN_PROGRESS) {
            if (state == LoginState.F_ACCOUNTNUM)
                formatter.printToScreen("loginFailedAccountNum", null);
            String input = scanner.nextLine();
            if (input.equals("cancel")) return true;
            try {
                accountNum = Integer.parseInt(input);
            } catch (Exception e) {
                state = LoginState.F_ACCOUNTNUM;
            }
            state = authServ.validateAccountNum(accountNum) ? LoginState.IN_PROGRESS : LoginState.F_ACCOUNTNUM;
        }
        formatter.printToScreen("login2",String.valueOf(accountNum));
        //stage 2 collect & validate login
        while (state != LoginState.COMPLETE) {
            if (state == LoginState.F_BADLOGIN) formatter.printToScreen("loginFailedPin",null);
            String input = scanner.nextLine();
            if (input.equals("cancel")) return true;
            //attempt to sign in
            state = authServ.signIn(accountNum,input) ? LoginState.COMPLETE : LoginState.F_BADLOGIN;
        }
        formatter.printToScreen("loginSuccess",null);
        return false;
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

    private void transfer() {
        TransferState state = TransferState.INIT;
        int fromAccNum = authServ.getAuthUserAccountNumber();
        double transferAmt = -1;
        int toAccNum = -1;
        //stage 1 get account to transfer to
        while (state != TransferState.IN_PROGRESS) {
            String stage1Screen = (state == TransferState.INIT) ? "transfer" : "transferFailedAccNum";
            formatter.printToScreen(stage1Screen,null);
            String input1 = scanner.nextLine();
            //validate input
            try {
                toAccNum = Integer.parseInt(input1);
            } catch (Exception e) {
                state = TransferState.F_ACCOUNTNUM;
            }
            //validate account number exists
            state = transServ.accountNumExists(toAccNum) ? TransferState.IN_PROGRESS : TransferState.F_ACCOUNTNUM;
        }
        //stage 2 validate payment
        while (state != TransferState.COMPLETE) {
            String stage2Screen = (state == TransferState.IN_PROGRESS) ? "transfer2" :
                (state == TransferState.F_INVALID) ? "transferFailed" : "transferFailedInsufficient";
            formatter.printToScreen(stage2Screen, Integer.toString(fromAccNum));
            String input2 = scanner.nextLine();
            //validate input
            try {
                transferAmt = Double.parseDouble(input2);
            } catch (Exception e) {
                state = TransferState.F_INVALID;
            }
            state = transServ.transfer(fromAccNum,toAccNum,transferAmt);
        }
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.printToScreen("transferSuccess",usFormat.format(transferAmt));
    }

    private void history(int pageNum) {
        int accNum = authServ.getAuthUserAccountNumber();
        List<Transaction> transactions = transServ.getHistory(accNum,pageNum);
        formatter.printHistoryTableToScreen(transactions,pageNum,transServ.getHistoryMaxPageNumber(accNum));
    }
}

package com.sylvie.clibank.api;

import com.sylvie.clibank.business.UserService;

public class CLIFormatter {

    private final UserService userServ;

    public CLIFormatter (UserService userServ) {
        this.userServ = userServ;
    }
    public record screenLine(String text, String offset){}

    public void printWelcomeScreen(int accountNum) {
        printScreenLines(new screenLine[]{
            getAccountNumScreenLine(accountNum),
            getHorizScreenLine(),
            new screenLine("Welcome to the Bank of CLI!","Center"),
            getBlankScreenLine(),
            new screenLine("Use the \"help\" command to see available commands","Center")
        });
    }

    public void printAuthedHelpScreen() {
        printScreenLines(new screenLine[] {
            new screenLine("Help Page","Center"),
            new screenLine("","Center")
        });
    }

    public void printNoAuthHelpScreen() {
        printScreenLines(new screenLine[] {
            new screenLine("Help Page - Available Commands", "Center"),
            getBlankScreenLine(),
            new screenLine("* Login - Authenticate with Account # and PIN.","Left"),
            new screenLine("* Register - Create a new account with PIN.", "Left")
        });
    }

    public void printRegisterScreen(boolean failedPin) {
        if (!failedPin) {
            printScreenLines(new screenLine[]{
                getBlankScreenLine(),
                new screenLine("Register for a Bank of CLI account!","Center"),
                getBlankScreenLine(),
                new screenLine("Please enter your desired PIN code below...","Center"),
                new screenLine("(must be 4 digits and a positive number)","Center")
        });}
        else {
            printScreenLines(new screenLine[]{
                getBlankScreenLine(),
                new screenLine("Register for a Bank of CLI account!","Center"),
                getBlankScreenLine(),
                new screenLine("Invalid PIN, try again...","Center"),
                new screenLine("(must be 4 digits and a positive number)","Center")
            });
        }
    }

    public void printSuccessfulRegistrationScreen(int accountNum) {
        printScreenLines(new screenLine[]{
            getAccountNumScreenLine(accountNum),
            getHorizScreenLine(),
            new screenLine("You've created an account, you're logged in now!","Center"),
            new screenLine("Please note your credentials for future login:","Center"),
            new screenLine("Account Number: " + accountNum + " | PIN Code: ****", "Center"),
        });
    }

    public void printLoginScreen1() {
        printScreenLines(new screenLine[] {
            getBlankScreenLine(),
            new screenLine("Login to your Bank of CLI account!","Center"),
            getBlankScreenLine(),
            new screenLine("Please enter your account number below...","Center"),
            getBlankScreenLine()
        });
    }
    public void printLoginScreen2() {
        printScreenLines(new screenLine[] {
                getBlankScreenLine(),
                new screenLine("Login to your Bank of CLI account!","Center"),
                getBlankScreenLine(),
                new screenLine("Please enter your PIN code below...","Center"),
                getBlankScreenLine()
        });
    }

    public void printLoginScreenFail() {
        printScreenLines(new screenLine[] {
                getBlankScreenLine(),
                new screenLine("Incorrect Login, Try again...","Center"),
                getBlankScreenLine(),
                new screenLine("Please enter your account number below...","Center"),
                getBlankScreenLine()
        });
    }

    public void printLoginSuccessfulScreen(int accountNum) {
        printScreenLines(new screenLine[] {
                getAccountNumScreenLine(accountNum),
                getHorizScreenLine(),
                getBlankScreenLine(),
                new screenLine("Logged into your Bank of CLI account!","Center"),
                getBlankScreenLine()
        });
    }

    public void printLogoutScreen() {
        printScreenLines(new screenLine[] {
            getAccountNumScreenLine(-1),
            getHorizScreenLine(),
            new screenLine("You've logged out of your account","Center"),
            getBlankScreenLine(),
            new screenLine("Use the \"help\" command to see available commands","Center")
        });
    }

    public screenLine getBlankScreenLine() {
        return new screenLine("","Center");
    }

    public screenLine getHorizScreenLine() {
        return new screenLine("--------------------------------------------------------","Center");
    }

    public screenLine getAccountNumScreenLine(int accountNum) {
        return new screenLine("Account #: " + (accountNum == -1 ? "--" : accountNum),"Right");
    }

    //58 chars per line 2 always occupied by vertical lines
    //usable space 56 chars

    public void printScreenLines(screenLine[] lines) {
        System.out.println("----------------------------------------------------------");
        for (screenLine line : lines) {
            int length = line.text.length();
            int availPadding = 56 - length;
            int paddingLeft = 0;
            int paddingRight = 0;
            if (line.offset == "Center") {
                if (availPadding % 2 == 0) {
                    paddingLeft = availPadding / 2;
                    paddingRight = availPadding - paddingLeft;
                } else {
                    paddingLeft = (availPadding / 2) + 1;
                    paddingRight = availPadding - paddingLeft;
                }
            } else if (line.offset == "Left") {
                paddingLeft = availPadding / 4;
                paddingRight = availPadding - paddingLeft;
            } else if (line.offset == "Right") {
                paddingRight = availPadding / 4;
                paddingLeft = availPadding - paddingRight;
            }
            System.out.print("|");
            while (paddingLeft > 0) {
                System.out.print(" ");
                paddingLeft--;
            }
            System.out.print(line.text);
            while (paddingRight > 0) {
                System.out.print(" ");
                paddingRight--;
            }
            System.out.println("|");
        }
        System.out.println("----------------------------------------------------------");
        System.out.print("  > ");
    }
}

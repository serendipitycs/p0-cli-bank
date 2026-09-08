package com.sylvie.clibank.api;

import java.sql.*;
import java.util.Scanner;


public class MainInterface {
    private static boolean quitApplication = false;
    public static void main(String[] args) {
        // Resizes the terminal window to 40 rows and 120 columns
        System.out.print("\033[8;40;120t");
        CLIFormatter formatter = new CLIFormatter();
        formatter.printWelcomeScreen();
        Scanner scanner = new Scanner(System.in);

        while (!quitApplication) {
            //main repl loop
            String input = scanner.nextLine();

            switch (input) {
                case "help":
                    break;
                case "login":
                    break;
                case "logout":
                    break;
                case "register":
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

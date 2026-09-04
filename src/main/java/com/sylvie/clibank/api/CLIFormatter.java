package com.sylvie.clibank.api;

public class CLIFormatter {
    CLIFormatter() {}
    public void printWelcomeScreen() {
        System.out.println("----------------------------------------------------------");
        System.out.println("|                                   Account #: --        |");
        System.out.println("|                                                        |");
        System.out.println("|              Welcome to the Bank of CLI!               |");
        System.out.println("|                                                        |");
        System.out.println("|    Use the \"help\" command to see available commands    |");
        System.out.println("----------------------------------------------------------");
        System.out.print("  > ");
    }
}

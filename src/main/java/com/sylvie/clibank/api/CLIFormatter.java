package com.sylvie.clibank.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sylvie.clibank.business.AuthenticationService;
import com.sylvie.clibank.business.UserService;
import com.sylvie.clibank.repository.models.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CLIFormatter {

    private final UserService userServ;
    private final AuthenticationService authServ;
    private final Map<String, PrintBox> textBoxes;


    public CLIFormatter (UserService userServ, AuthenticationService authServ) {
        this.userServ = userServ;
        this.authServ = authServ;
        textBoxes = initializeTextBoxes();
    }

    private Map<String, PrintBox> initializeTextBoxes() {
        Gson gson = new Gson();
        try (
            InputStream inputStream = getClass().getResourceAsStream("/strings.json");
            InputStreamReader reader = new InputStreamReader(inputStream)) {
            Type type = new TypeToken<Map<String, PrintBox>>(){}.getType();
            return gson.fromJson(reader,type);
        } catch (Exception e) {
            //logging
        }
        return null;
    }

    public void printToScreen(String name, String customReplace) {
        List<Line> lines = textBoxes.get(name).getAllLines();
        System.out.println(getTitleLine(textBoxes.get(name).getTitle()));
        for (Line line : lines) {
            String text = replaceSpecialCharacters(line.getText(),customReplace);
            Map<String,Integer> paddings = calculatePadding(text,line.getCentering(),56);
            System.out.print("│");
            System.out.print(" ".repeat(paddings.get("left")));
            System.out.print(text);
            System.out.print(" ".repeat(paddings.get("right")));
            System.out.println("│");
        }
        System.out.println("└────────────────────────────────────────────────────────┘");
        System.out.print("  > ");
    }

    public void printHistoryTableToScreen(List<Transaction> transactions, int pageNum, int maxPages) {
        System.out.println("┌──────────┬─────────────[History]──┬────────────────────┐");
        System.out.println("│___Type___│_________Amount_________│_____Timestamp______│");
        for (Transaction t : transactions) {
            System.out.print("│");
            //Rename TransferTo and TransferFrom to "Transfer"
            String typeFinal = t.getType().equals("TransferTo") || t.getType().equals("TransferFrom") ? "Transfer" : t.getType();
            Map<String, Integer> typePadding = calculatePadding(typeFinal,"Center",10);
            System.out.print(" ".repeat(typePadding.get("left")));
            System.out.print(typeFinal);
            System.out.print(" ".repeat(typePadding.get("right")));
            System.out.print("│");
            NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
            char symbol = t.getType().equals("TransferFrom") || t.getType().equals("Deposit") ? '+' : '-';
            String finalAmount = symbol + usFormat.format(t.getAmount());
            Map<String, Integer> amountPadding = calculatePadding(finalAmount,"Center",24);
            System.out.print(" ".repeat(amountPadding.get("left")));
            System.out.print(finalAmount);
            System.out.print(" ".repeat(amountPadding.get("right")));
            System.out.print("│");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma", Locale.US);
            String finalTimestamp = t.getTimestamp().toLocalDateTime().format(formatter);
            Map<String, Integer> timestampPadding = calculatePadding(finalTimestamp,"Center",20);
            System.out.print(" ".repeat(timestampPadding.get("left")));
            System.out.print(finalTimestamp);
            System.out.print(" ".repeat(timestampPadding.get("right")));
            System.out.println("│");
        }
        String pageCounter = "[Page " + pageNum + "/" + maxPages + "]";
        System.out.print("└──────────┴────────────────────────┴");
        System.out.print("─".repeat(18-pageCounter.length()));
        System.out.print(pageCounter);
        System.out.println("──┘");
        System.out.print("  > ");
    }

    public String getTitleLine(String title) {
        String text = "[" + title + "]";
        Map<String,Integer> padding = calculatePadding(text,"Center",56);
        StringBuilder sb = new StringBuilder();
        sb.append("┌");
        sb.repeat("─",padding.get("left"));
        sb.append(text);
        sb.repeat("─",padding.get("right"));
        sb.append("┐");
        return sb.toString();
    }

    public String replaceSpecialCharacters(String text, String specialReplace) {
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
        text = text.replace("---","────────────────────────────────────────────────────────");
        text = text.replace("$header", authServ.isAuthenticatedUser() ?
                "Balance: $bal   |   Account #: $accnum" :
                "Please login to see account information.");
        text = text.replace("$bal", usFormat.format(userServ.getBalance(authServ.getAuthUserAccountNumber())));
        text = text.replace("$accnum", Integer.toString(authServ.getAuthUserAccountNumber()));
        text = (specialReplace != null) ? text.replace("$$$",specialReplace) : text;
        return  text;
    }

    public Map<String,Integer> calculatePadding(String text, String centering, int availPadding) {
        Map<String,Integer> paddings = new HashMap<>();
        availPadding -= text.length();
        switch (centering) {
            case "Left":
                paddings.put("left",1);
                paddings.put("right",availPadding-1);
                break;
            case "Center":
                if (availPadding % 2 == 0) {
                    paddings.put("left",availPadding / 2);
                    paddings.put("right",availPadding / 2);
                } else {
                    int paddingLeft = availPadding / 2 + 1;
                    paddings.put("left",paddingLeft);
                    paddings.put("right",availPadding - paddingLeft);
                }
                break;
            default:
                break;
        }
        return paddings;
    }
}

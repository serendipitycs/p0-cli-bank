package com.sylvie.clibank.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sylvie.clibank.business.AuthenticationService;
import com.sylvie.clibank.business.UserService;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.NumberFormat;
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

        System.out.println("----------------------------------------------------------");
        for (Line line : lines) {
            String text = replaceSpecialCharacters(line.getText(),customReplace);
            Map<String,Integer> paddings = calculatePadding(text,line.getCentering());
            System.out.print("|");
            System.out.print(" ".repeat(paddings.get("left")));
            System.out.print(text);
            System.out.print(" ".repeat(paddings.get("right")));
            System.out.println("|");
        }
        System.out.println("----------------------------------------------------------");
        System.out.print("  > ");
    }

    public String replaceSpecialCharacters(String text, String specialReplace) {
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
        text = text.replace("---","--------------------------------------------------------");
        text = text.replace("$header", authServ.isAuthenticatedUser() ?
                "Balance: $bal   |   Account #: $accnum" :
                "Please signin to see account information.");
        text = text.replace("$bal", usFormat.format(userServ.getBalance(authServ.getAuthUserAccountNumber())));
        text = text.replace("$accnum", Integer.toString(authServ.getAuthUserAccountNumber()));
        text = (specialReplace != null) ? text.replace("$$$",specialReplace) : text;
        return  text;
    }

    public Map<String,Integer> calculatePadding(String text, String centering) {
        Map<String,Integer> paddings = new HashMap<>();
        int availPadding = 56 - text.length();
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

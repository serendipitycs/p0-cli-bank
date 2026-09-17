package com.sylvie.clibank.api;

import java.util.Arrays;
import java.util.List;

public class PrintBox {
    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private Line line5;
    private String title;

    public List<Line> getAllLines() {return Arrays.asList(line1,line2,line3,line4,line5);}
    public String getTitle() {return title;}
}

class Line {
    private String text;
    private String centering;

    public String getText() { return text; }
    public String getCentering() { return centering; }
}

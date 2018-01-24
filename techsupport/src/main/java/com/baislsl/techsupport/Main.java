package com.baislsl.techsupport;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);
    private final static String dataPath = "/dev-v1.1.json";
    private static QuestionDictionary dict;
    private static TechSupport techSupport;

    static {
        try {
            dict = QuestionDictionary.build(new InputStreamReader(Main.class.getResourceAsStream(dataPath)));
            techSupport = new TechSupport(dict);
        } catch (IOException e) {
            log.error("error read in " + dataPath, e);
        } catch (ParseException e) {
            log.error("json parse error", e);
        }
        if(dict == null) System.exit(1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        Hint.printInputHint();
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            Hint.printInputAnswerHint();
            Question p = techSupport.analyze(input);

            System.out.println(String.format("Best match problem is:\n\t%s\nThe answer of this problem is: \n\t%s", p.title, p.answer));

            Hint.printInputHint();
        }
    }
}

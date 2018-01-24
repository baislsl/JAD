package com.baislsl.techsupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Hint {
    private final static Logger log = LoggerFactory.getLogger(Hint.class);
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(Hint.class.getResourceAsStream("/default.properties"));
        } catch (IOException e) {
            log.error("Error loading properties file", e);
        }
    }

    public static void printInputHint(){
        System.out.println(properties.getProperty("hint.input"));
    }

    public static void printInputAnswerHint(){
        System.out.println(properties.getProperty("hint.answer"));
    }
}

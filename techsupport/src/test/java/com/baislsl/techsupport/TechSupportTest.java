package com.baislsl.techsupport;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class TechSupportTest {
    private final static String dataPath = "/dev-v1.1.json";
    private QuestionDictionary dict;
    private TechSupport techSupport;


    @Before
    public void init() throws Exception {
        dict = QuestionDictionary.build(new InputStreamReader(Main.class.getResourceAsStream(dataPath)));
        techSupport = new TechSupport(dict);
    }


    @Test
    public void analyzeTest() throws Exception {
        Question q1 = techSupport.analyze("schrodinger equation Newtonian variables");
        assertEquals("What type of measurements result under Schrodinger equations when using operators instead of Newtonian variables?",
                q1.title);
        assertEquals("quantized", q1.answer);

        Question q2 = techSupport.analyze("Midlothian County Buildings");
        assertEquals("What do the former Midlothian County Buildings face?", q2.title);
        assertEquals("Parliament Square, High Street and George IV Bridge in Edinburgh",
                q2.answer);


        //
        Question q3 = techSupport.analyze("building on George IV Bridge when the Parliament");
        assertEquals("What happened to the building on George IV Bridge when the Parliament was done with it?", q3.title);
        assertEquals("demolished",
                q3.answer);

    }
}
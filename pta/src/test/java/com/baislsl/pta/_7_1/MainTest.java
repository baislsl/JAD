package com.baislsl.pta._7_1;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MainTest {
    private final static Logger logger = LoggerFactory.getLogger(MainTest.class);

    @Test
    public void cal() throws Exception {
        Map<String, Integer> testCase = new HashMap<String, Integer>();


        testCase.put("(2+32)/2-6", 11);
        testCase.put("1+2*4/(45-44*1)-5+4/2", 6);
        testCase.put("1+2+1+3", 7);
        testCase.put("1*2*1*3", 6);
        testCase.put("1+7%3*3", 4);
        testCase.put("1+(1-6)", -5);

        for (Map.Entry<String, Integer> e : testCase.entrySet()){
            // assertTrue(e.getValue() == new Main().cal(e.getKey()));
            assertEquals(e.getValue().longValue(), (long)(new Main().cal(e.getKey())));
        }
    }

}
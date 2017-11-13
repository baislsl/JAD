package com.baislsl.pta._7_2;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void cal() throws Exception {
        Map<String, Integer> testCase = new HashMap<String, Integer>();


        testCase.put("-(1-7)", 6);
        testCase.put("-(1-7)/(-3)*3/6+3", 2);
        testCase.put("1+2+1+3", 7);

        testCase.put("((-2+32)/2)-6", 9);
        testCase.put("-1+2*4/(45-44*1)-5+4/2", 4);
        testCase.put("1*2*1*3", 6);
        testCase.put("2*--1+(1-7)", -4);

        for (Map.Entry<String, Integer> e : testCase.entrySet()){
            // assertTrue(e.getValue() == new Main().cal(e.getKey()));
            assertEquals(e.getValue().longValue(), (long)(new Main().cal(e.getKey())));
        }
    }

}
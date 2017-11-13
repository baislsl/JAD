package com.baislsl.pta._7_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.println(new Main().cal(line));
    }

    private String replace(String s, int left) {
        int counter = 1;
        int right = left + 1;
        while (counter != 0) {
            if (s.charAt(right) == '(') counter++;
            else if (s.charAt(right) == ')') counter--;
            ++right;
        }
        int i = cal(s.substring(left + 1, right - 1));
        return s.substring(0, left) + Integer.toString(i) + s.substring(right);

//        if (i < 0) {
//            return s.substring(0, left) + "-1*" + Integer.toString(-i) + s.substring(right);
//        } else {
        //}
    }

    public int cal(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                s = replace(s, i);
            }
        }

        List<String> ss = new ArrayList<>();
        StringBuilder next = new StringBuilder();
        int i = 0;
        while (s.charAt(i) == '+' || s.charAt(i) == '-') {
            ss.add(s.charAt(i) + "1");
            ss.add("*");
            ++i;
        }
        for (; i < s.length(); i++) {
            if (isOp(s.charAt(i))) {
                if ("*/%".contains(s.charAt(i) + "")) {
                    char cc = s.charAt(i);
                    i++;
                    while (s.charAt(i) == '+' || s.charAt(i) == '-') {
                        ss.add(s.charAt(i) + "1");
                        ss.add("*");
                        ++i;
                    }
                    --i;

                    ss.add(next.toString());
                    ss.add(cc + "");
                    next = new StringBuilder();
                } else {
                    ss.add(next.toString());
                    ss.add("+");
                    while (s.charAt(i) == '+' || s.charAt(i) == '-') {
                        ss.add(s.charAt(i) + "1");
                        ss.add("*");
                        ++i;
                    }
                    --i;
                    next = new StringBuilder();
                }
            } else {
                next.append(s.charAt(i));
            }
        }
        ss.add(next.toString());


        i = 1;
        int ans = Integer.parseInt(ss.get(0));
        while (i < ss.size() && "*/%".contains(ss.get(i))) {
            ans = func(ss.get(i), ans, Integer.parseInt(ss.get(i + 1)));
            i += 2;
        }

        while (i < ss.size()) {
            String op = ss.get(i);
            int nxt = Integer.parseInt(ss.get(i + 1));
            i += 2;
            while (i < ss.size() && "*/%".contains(ss.get(i))) {
                nxt = func(ss.get(i), nxt, Integer.parseInt(ss.get(i + 1)));
                i += 2;
            }

            ans = func(op, ans, nxt);
        }

        return ans;
    }


    private int func(String s, int a, int b) {
        return func(s.trim().charAt(0), a, b);
    }

    private int func(char cc, int a, int b) {
        switch (cc) {
            case '-':
                return a - b;
            case '+':
                return a + b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            case '%':
                return a % b;
            default:
                return 0;
        }
    }

    private boolean isOp(char cc) {
        String op = "+-*/%";
        return op.contains(cc + "");
    }
}

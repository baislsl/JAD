package com.baislsl.pta.httpd_anal;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 10.180.17.246 - - [24/Dec/2017:09:31:35 +0800] "GET /apple-touch-icon.png HTTP/1.1"
 * 404 509 "-" "Safari/13604.4.7.1.3 CFNetwork/893.13.1 Darwin/17.3.0 (x86_64)"
 * <p>
 * <p>
 * 10.180.17.246 - - [24/Dec/2017:09:31:44 +0800] "POST /login.php HTTP/1.1"
 * 302 452 "http://fm.zju.edu.cn/" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2)
 * AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
 * <p>
 * <p>
 * 10.180.17.246 - - [24/Dec/2017:09:32:17 +0800] "GET /showCourse.php?id=57 HTTP/1.1"
 * 200 1894 "http://fm.zju.edu.cn/index.php" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2)
 * AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
 * <p>
 * ---------------------
 * <p>
 * 10.180.17.246 - - [24/Dec/2017:09:31:44 +0800] "POST /login.php HTTP/1.1"
 * 302 452 "http://fm.zju.edu.cn/" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2)
 * AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
 * <p>
 * 10.180.17.246 - - [24/Dec/2017:09:31:44 +0800] "GET /index.php HTTP/1.1"
 * 200 1400 "http://fm.zju.edu.cn/" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2)
 * AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
 * <p>
 * 10.180.17.246 - - [24/Dec/2017:09:32:17 +0800] "GET /showCourse.php?id=57 HTTP/1.1"
 * 200 1894 "http://fm.zju.edu.cn/index.php" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2)
 * AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
 * <p>
 * 10.180.17.246 - - [24/Dec/2017:09:33:14 +0800] "GET /showCourse.php?id=126 HTTP/1.1"
 * 200 3765 "http://fm.zju.edu.cn/showCourse.php?id=57"
 * "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
 */

public class Main {
    private Pattern pattern = Pattern.compile(
            //10.180.17.246 - - [24/Dec/2017:09:32:17 +0800] "GET /showCourse.php?id=57 HTTP/1.1"
           //  "[.|\\d]+\\s+-\\s+-\\s+\\[\\d+/\\w+/\\d+:\\d+:\\d+:\\d+\\s+\\+\\d+]\\s+\"\\w+\\s+" +
              //      ".*\"\\w+\\s+(/\\S+)\\s+HTTP/\\d\\.\\d\".*");
                    ".*GET+\\s+(\\S+)\\s+\\S+.*");

    private Pattern pattern2 = Pattern.compile(
            //10.180.17.246 - - [24/Dec/2017:09:32:17 +0800] "GET /showCourse.php?id=57 HTTP/1.1"
            //  "[.|\\d]+\\s+-\\s+-\\s+\\[\\d+/\\w+/\\d+:\\d+:\\d+:\\d+\\s+\\+\\d+]\\s+\"\\w+\\s+" +
            //      ".*\"\\w+\\s+(/\\S+)\\s+HTTP/\\d\\.\\d\".*");
            ".*POST+\\s+(\\S+)\\s+\\S+.*");

    private List<String> readin() {
        List<String> ans = new ArrayList<>();
          Scanner scanner = new Scanner(System.in);

//        Scanner scanner = null;
//        try {
//            scanner = new Scanner(new FileInputStream("/home/baislsl/java/homework/pta/src/main/resources/http_in.txt"));
//        } catch (Exception e) {
//
//        }

        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            ans.add(line);
        }
        return ans;
    }

    public String toRawAddress(String address) {
        Matcher matcher = pattern.matcher(address);
        if (matcher.find()) {
            String s = matcher.group(1);
            if (s.contains("?")) s = s.substring(0, s.indexOf('?'));
            return s;
        } else {
            Matcher matcher2 = pattern2.matcher(address);
            if(matcher2.find()){
                String s = matcher2.group(1);
                if (s.contains("?")) s = s.substring(0, s.indexOf('?'));
                return s;
            }
        }
        return null;

    }

    private Map<String, Integer> sort(List<String> urls) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : urls) {
            String raw = toRawAddress(s);
            if(raw == null) continue;
            if (map.containsKey(raw)) {
                int i = map.get(raw);
                map.put(raw, i + 1);
            } else {
                map.put(raw, 1);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        Main t = new Main();
        // Map<String, Integer> map = t.sort(t.test());
        Map<String, Integer> map = t.sort(t.readin());

        int max = Integer.MIN_VALUE;
        for (int i : map.values()) {
            max = Math.max(max, i);
        }

        List<String> urls = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == max) {
                urls.add(entry.getKey());
            }
        }

        urls.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        for (String url : urls) {
            System.out.println(max + ":" + url);
        }


    }
}

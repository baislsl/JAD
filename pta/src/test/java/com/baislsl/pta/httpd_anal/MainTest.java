package com.baislsl.pta.httpd_anal;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    private final static Logger logger = LoggerFactory.getLogger(MainTest.class);
    @Test
    public void test() {
        List<String> ls = new ArrayList<>();
        ls.add("10.180.17.246 - - [24/Dec/2017:09:31:44 +0800] \"POST /login.php HTTP/1.1\" 302 452 \"http://fm.zju.edu.cn/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7\"\n");
        ls.add("10.180.17.246 - - [24/Dec/2017:09:31:44 +0800] \"GET /index.php HTTP/1.1\" 200 1400 \"http://fm.zju.edu.cn/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7\"\n");
        ls.add("10.180.17.246 - - [24/Dec/2017:09:32:17 +0800] \"GET /showCourse.php?id=57 HTTP/1.1\" 200 1894 \"http://fm.zju.edu.cn/index.php\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7\"\n");
        ls.add("10.180.17.246 - - [24/Dec/2017:09:33:14 +0800] \"GET /showCourse.php?id=126 HTTP/1.1\" 200 3765 \"http://fm.zju.edu.cn/showCourse.php?id=57\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7\"\n");
        ls.add("10.180.17.246 - - [24/Dec/2017:09:31:35 +0800] \"GET /apple-touch-icon.png HTTP/1.1\" 404 509 \"-\" \"Safari/13604.4.7.1.3 CFNetwork/893.13.1 Darwin/17.3.0 (x86_64)\"\n");
        ls.add("10.180.17.246 - - [24/Dec/2017:09:31:44 +0800] \"POST /login.php HTTP/1.1\" 302 452 \"http://fm.zju.edu.cn/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7\"\n");
        ls.add("10.180.17.246 - - [24/Dec/2017:09:32:17 +0800] \"GET /showCourse.php?id=57 HTTP/1.1\" 200 1894 \"http://fm.zju.edu.cn/index.php\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7\"\n");
        List<String> rs = new ArrayList<>();
        Main m = new Main();
        for (String s : ls) {
            String r = m.toRawAddress(s);
            logger.info(r);
            rs.add(r);
        }

        assertEquals(rs.get(0), "/login.php");
        assertEquals(rs.get(1), "/index.php");
        assertEquals(rs.get(2), "/showCourse.php");
        assertEquals(rs.get(3), "/showCourse.php");
        assertEquals(rs.get(4), "/apple-touch-icon.png");
        assertEquals(rs.get(5), "/login.php");
        assertEquals(rs.get(6), "/showCourse.php");
    }

}
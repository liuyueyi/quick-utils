package com.github.liuyueyi.tools.test.str;

import org.junit.Test;

import javax.xml.soap.Text;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 字符串替换测试类
 *
 * @author yihui
 * @date 21/8/8
 */
public class StrFormatTest {

    @Test
    public void testStrFormat() {
        System.out.println(String.format("hello %s", 120));
        System.out.println(String.format("hello %s", true));
        System.out.println(String.format("hello %s", new int[]{1, 2, 3}));
        System.out.println(String.format("hello %s", Arrays.asList(1, 2, 3)));
        System.out.println(String.format("hello %s", 0x12));
    }

    @Test
    public void testNumFormat() {
        try {
            System.out.println(String.format("hello %d", 0x11));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println(String.format("hello %d", 1.1F));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println(String.format("hello %d", "10"));
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Test
    public void testArgNumNotMatch() {
        try {
            System.out.println(String.format("hello %s %s", "yihui"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("---------------- ");

        try {
            System.out.println(String.format("hello %s", "yihuihui", "blog"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTextFormat() {
        String ans = MessageFormat.format("hello {0}, wechart site {0}{1}", "一灰灰", "blog");
        System.out.println(ans);
    }

    @Test
    public void testMiss() {
        System.out.println(MessageFormat.format("hello }", 123));
        System.out.println(MessageFormat.format("hello '{' {0} world",  456));
        System.out.println(MessageFormat.format("hello { world",  456));
    }

    @Test
    public void testDot() {
        System.out.println(MessageFormat.format("hello {0}, I'm {1}", "一灰灰", "blog"));
        System.out.println(MessageFormat.format("hello {0}, I''m {1}", "一灰灰", "blog"));

        MessageFormat messageFormat = new MessageFormat("!23");
        System.out.println(messageFormat.format("hello {}, world", "yihuihui"));
        System.out.println(messageFormat.format("hello {} world {}", "yihuhui", "blog"));
    }
}

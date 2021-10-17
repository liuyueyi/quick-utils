package com.github.liuyueyi.tools.test.collection;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.junit.Test;

import java.util.*;

/**
 * 排序测试类
 *
 * @author yihui
 * @date 2021/8/13
 */
public class SortTest {

    @Test
    public void testSort() {
        List<Integer> list = new ArrayList<>();
        list.add(30);
        list.add(10);
        list.add(20);

        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        System.out.println("after sort: " + list);

        // ---
        list.add(Integer.MIN_VALUE);
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        System.out.println("after sort2: " + list);

        // 默认排序方式
        list.sort(null);
        System.out.println("sort: " + list);

        list.add(1);

        Collections.sort(list, new ComparableComparator<>());
        System.out.println(list);
    }

    public static class Demo implements Comparable<Demo> {
        int code;
        int age;

        public Demo(int code, int age) {
            this.code = code;
            this.age = age;
        }

        @Override
        public int compareTo(Demo o) {
            if (code == o.code) {
                return 0;
            } else if (code < o.code) {
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        public String toString() {
            return "Demo{" +
                    "code=" + code +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    public void testDemoSort() {
        List<Demo> list = new ArrayList<>();
        list.add(new Demo(10, 30));
        list.add(new Demo(12, 10));
        list.add(new Demo(11, 20));
        // 默认根据 code 进行升序比较
        list.sort(null);
        System.out.println("sort by code: " + list);

        list.sort(Comparator.comparingInt(o -> o.age));
        System.out.println("sort by age: " + list);


        list.sort(Comparator.comparingInt(o -> -o.age));
        System.out.println("sort by age: " + list);
    }

}

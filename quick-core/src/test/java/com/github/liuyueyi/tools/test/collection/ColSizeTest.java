package com.github.liuyueyi.tools.test.collection;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yihui
 * @date 2021/8/15
 */
public class ColSizeTest {

    @Test
    public void testList() {
        ArrayList<String> aryList = new ArrayList<>(10);
        aryList.add("hello");
        List<String> linkedList = new LinkedList<>();

        List<String> values = Arrays.asList("12", "220", "123");
        CopyOnWriteArrayList<String> cList = new CopyOnWriteArrayList<>(values);
        cList.add("hello");

        aryList.subList(10, 10);
    }

    @Test
    public void testMap() {
        HashMap<String, String> map = new HashMap<>(4);
        map.put("a", "a");
        map.put("b", "c");
        map.put("c", "c");
        map.put("d", "c");
        map.put("e", "c");
        map.put("f", "c");
        map.put("g", "c");

        Maps.newHashMapWithExpectedSize(6);

        new LinkedHashMap<>(10);
        TreeMap treeMap = new TreeMap<>();
        treeMap.put("A", "b");

        Set<String> set = new HashSet<>();
    }

    public List<Integer> minStack(List<Integer> list, int value, int stackSzie) {
        list.add(value);
        if (list.size() < stackSzie) {
            return list;
        }
        list.sort(null);
        return new ArrayList<>(list.subList(0, stackSzie));
    }

    @Test
    public void testFix() {
        List<Integer> list = new ArrayList<>();
        for (int i = Integer.MAX_VALUE; i > Integer.MIN_VALUE; i--) {
            list.add(i);
            list = minStack(list, i, 5);
            System.out.println(list);
        }
    }
}

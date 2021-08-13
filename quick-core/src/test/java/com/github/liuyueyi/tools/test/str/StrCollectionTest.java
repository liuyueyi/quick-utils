package com.github.liuyueyi.tools.test.str;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yihui
 * @date 21/8/8
 */
public class StrCollectionTest {

    public List<String> str2list(String str, String split) {
        String[] cells = str.split(split);
        List<String> list = new ArrayList<>();
        for (String cell : cells) {
            list.add(cell);
        }
        return list;
    }

    public List<String> str2list2(String str, String split) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, str.split(split));
        return list;
    }

    public List<Integer> str2intList(String str, String split) {
        return Stream.of(str.split(split))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::valueOf).collect(Collectors.toList());
    }

    public List<Integer> str2intListV2(String str, String split) {
        return Splitter.on(split).splitToStream(str)
                .map(String::trim).filter(s -> !s.isEmpty())
                .map(Integer::valueOf).collect(Collectors.toList());
    }

    public List<Integer> str2intListV3(String str, String split) {
        List<Integer> result = new ArrayList<>();
        CollectionUtils.collect(Arrays.asList(str.split(split)), Integer::valueOf, result);
        return result;
    }

    @Test
    public void str2list() {
        String str = "1,2,3";
        System.out.println(str2intListV3(str, ","));
    }


    public String list2str(List<String> list, String split) {
        StringBuilder builder = new StringBuilder();
        for (String str: list) {
            builder.append(str).append(split);
        }
        return builder.substring(0, builder.length() - 1);
    }

    public String list2str2(List<String> list, String split) {
        return String.join(split, list);
    }

    public String list2str3(List<String> list, String split) {
        return Joiner.on(split).join(list);
    }

    public String list2str4(List<Integer> list, String split) {
        return Joiner.on(split).join(list);
    }

    @Test
    public void testList2Str() {
        List<String> list = Arrays.asList("1", "2", "3");
        System.out.println(list2str2(list, ","));

        List<Integer> l2 = Arrays.asList(1, 2, 3);
        System.out.println(list2str4(l2, ","));
    }

    @Test
    public void ary2list() {
        String[] ary = new String[]{"1", "a"};
        List<String> list = Arrays.asList(ary);
        System.out.println(list);

        list.set(1, "b");
        System.out.println();
        list.remove(0);
        System.out.println(list);
    }

    @Test
    public void ary2listV2() {
        String[] ary = new String[]{"1", "a"};
        List<String> out = new ArrayList<>(Arrays.asList(ary));
        out.add("hello");
        System.out.println(out);
    }

    @Test
    public void ary2listV3() {
        String[] ary = new String[]{"1", "a"};
        List<String> out = new ArrayList<>(ary.length);
        Collections.addAll(out, ary);
        out.add("hello");
        System.out.println(out);
    }

    @Test
    public void list2ary() {
        List<String> list = Arrays.asList("a", "b", "c");
        Object[] cell = list.toArray();

        String[] strCell = list.toArray(new String[]{});
        System.out.println("----");
    }


    public String join2(List<Integer> list) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Integer sub: list) {
            if (first) {
                first = false;
            } else {
                builder.append(",");
            }
            builder.append(sub);
        }
        return builder.toString();
    }

    public String join3(List<Integer> list) {
        StringJoiner joiner = new StringJoiner(",");
        for (Integer s : list) {
            joiner.add(String.valueOf(s));
        }
        return joiner.toString();
    }

    public String join4(List<Integer> list) {
       return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public String join5(List<Integer> list) {
        return Joiner.on(",").join(list);
    }

    @Test
    public void testJoin4() {
        Integer i = 10;
        Integer j = 20;
        Long a = 10L;
        System.out.println(join4(Arrays.asList(1, 2, 3, 4)));
    }
}

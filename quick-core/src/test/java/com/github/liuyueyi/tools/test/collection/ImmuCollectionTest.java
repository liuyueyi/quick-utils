package com.github.liuyueyi.tools.test.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.*;

/**
 * @author yihui
 * @date 2021/8/15
 */
public class ImmuCollectionTest {

    @Test
    public void testCol() {
        Collections.emptyMap();
        Collections.emptyList();
        Collections.emptySet();


        List<Integer> list = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
        System.out.println(list);
    }


    @Test
    public void testGuava() {
        List<Integer> list = ImmutableList.of(1, 2, 3);
        Set<Integer> set = ImmutableSet.of(1, 2, 3);
        Map<String, Integer> map = ImmutableMap.of("hello", 1, "world", 2);
    }

}

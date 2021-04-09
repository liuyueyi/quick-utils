package com.github.liuyueyi.tools.bean.test;

import com.github.liuyueyi.tools.bean.BeanUtils;
import com.github.liuyueyi.tools.bean.cglib.QuickBeanMapWrapper;
import com.github.liuyueyi.tools.bean.converter.ComposeConverter;
import com.github.liuyueyi.tools.bean.converter.impl.AtomicIntegerConverter;
import com.github.liuyueyi.tools.bean.converter.impl.BeanConverter;
import com.github.liuyueyi.tools.bean.test.model.AtomicTarget;
import com.github.liuyueyi.tools.bean.test.model.CamelTarget;
import com.github.liuyueyi.tools.bean.test.model.SameTarget;
import com.github.liuyueyi.tools.bean.test.model.Source;
import net.sf.cglib.beans.BeanMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class BeanUtilTest extends BasicTest {

    @Test
    public void basicTest() {
        Source source = genSource();
        SameTarget target1_1 = BeanUtils.copy(source, SameTarget.class);
        CamelTarget target1_2 = BeanUtils.copy(source, CamelTarget.class, false);
        CamelTarget target2_1 = BeanUtils.copy(source, CamelTarget.class, true);
        Map map1 = BeanUtils.copy(source, Map.class, false);
        Map map2 = BeanUtils.copy(source, Map.class, true);
        Map map3 = BeanUtils.copy(source, HashMap.class, true);
        Map map4 = BeanUtils.toMap(source, HashMap.class, QuickBeanMapWrapper.ParseType.TO_UNDER_LINE);
        System.out.println("source:" + source + "\ntarget1_1:" + target1_1 + "\ntarget1_2:" + target1_2 + "\ntarget2_1:" + target2_1
                + "\nmap1:" + map1 + "\nmap2:" + map2 + "\nmap3:" + map3 + "\nmap4:" + map4);
    }

    @Test
    public void testBeanMap() {
        // bean 转 map
        Source source = genSource();
        Map map = BeanMap.create(source);
        System.out.println(map);
    }

    @Test
    public void testConverter() {
        Source source = genSource();
        AtomicTarget atomicTargetNo = BeanUtils.copy(source, AtomicTarget.class, true);
        AtomicTarget atomicTarget = BeanUtils.copy(source, AtomicTarget.class, true,
                new ComposeConverter().addConverter(new AtomicIntegerConverter())
                        .addConverter(new BeanConverter()).camelEnable(true));
        System.out.println(source + "\n" + atomicTargetNo + "\n" + atomicTarget);
    }
}

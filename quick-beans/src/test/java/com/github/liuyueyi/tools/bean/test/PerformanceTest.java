package com.github.liuyueyi.tools.bean.test;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.StopWatch;
import com.github.liuyueyi.tools.bean.test.model.CamelTarget;
import com.github.liuyueyi.tools.bean.test.model.Source;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.ReflectUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

/**
 * 性能测试
 *
 * @author yihui
 * @date 2021/4/9
 */
public class PerformanceTest extends BasicTest {
    public <K, T> T apacheCopy(K source, Class<T> target) throws Exception {
        T res = (T) ReflectUtils.newInstance(target);
        // 注意，第一个参数为target，第二个参数为source
        // 与其他的正好相反
        BeanUtils.copyProperties(res, source);
        return res;
    }

    public <K, T> T pureCglibCopy(K source, Class<T> target) throws Exception {
        BeanCopier copier = BeanCopier.create(source.getClass(), target, false);
        T res = (T) ReflectUtils.newInstance(target);
        copier.copy(source, res, null);
        return res;
    }

    public <K, T> T huToolCopy(K source, Class<T> target) throws Exception {
        return BeanUtil.toBean(source, target);
    }

    public <K, T> T quickCopy(K source, Class<T> target, boolean camelEnable) throws Exception {
        return com.github.liuyueyi.tools.bean.BeanUtils.copy(source, target, camelEnable);
    }

    @Test
    public void copyTest() throws Exception {
        Source s = genSource();
        Class target = CamelTarget.class;
        Object ta = apacheCopy(s, target);
        Object th = huToolCopy(s, target);
        Object tc = pureCglibCopy(s, target);
        Object tq = quickCopy(s, target, true);
        System.out.println("source:\t" + s + "\napache:\t" + ta + "\nhutool:\t" + th
                + "\ncglib:\t" + tc + "\nquick:\t" + tq);
    }

    @Test
    public void testPerformance() throws Exception {
        Class target = CamelTarget.class;
        autoCheck(target, 10000);
        autoCheck(target, 10000);
        autoCheck(target, 10000_0);
        autoCheck(target, 20000_0);
        autoCheck(target, 50000_0);
        autoCheck(target, 10000_00);
    }

    private <T> void autoCheck(Class<T> target, int size) throws Exception {
        StopWatch stopWatch = new StopWatch();
        runCopier(stopWatch, "apacheCopy", size, (s) -> apacheCopy(s, target));
        runCopier(stopWatch, "huToolCopy", size, (s) -> huToolCopy(s, target));
        runCopier(stopWatch, "pureCglibCopy", size, (s) -> pureCglibCopy(s, target));
        runCopier(stopWatch, "quickCopy", size, (s) -> quickCopy(s, target, true));
        System.out.println((size / 10000) + "w -------- cost: " + stopWatch.prettyPrint());
    }

    private <T> void runCopier(StopWatch stopWatch, String key, int size, CopierFunc func) throws Exception {
        stopWatch.start(key);
        for (int i = 0; i < size; i++) {
            Source s = genSource();
            func.apply(s);
        }
        stopWatch.stop();
    }

    @FunctionalInterface
    public interface CopierFunc<T> {
        T apply(Source s) throws Exception;
    }

}

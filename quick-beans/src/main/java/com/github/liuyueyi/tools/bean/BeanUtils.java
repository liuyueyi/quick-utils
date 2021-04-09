package com.github.liuyueyi.tools.bean;

import com.github.liuyueyi.tools.bean.cglib.QuickBeanMapWrapper;
import com.github.liuyueyi.tools.bean.cglib.QuickExtendBeanCopier;
import com.github.liuyueyi.tools.bean.model.BeanCopierOptions;
import net.sf.cglib.core.Converter;
import net.sf.cglib.core.ReflectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class BeanUtils {
    private static Converter DEFAULT_CONVERTER = null;

    private static final Map<BeanCopierOptions, QuickExtendBeanCopier> beanCopierCache = new ConcurrentHashMap<>();

    public static <K, T> T copy(K source, Class<T> target) {
        return copy(source, target, false, false);
    }

    public static <K, T> T copy(K source, Class<T> target, Converter converter) {
        return copy(source, target, false, converter);
    }

    public static <K, T> T copy(K source, Class<T> target, boolean camel) {
        return copy(source, target, camel, false);
    }

    public static <K, T> T copy(K source, Class<T> target, boolean camel, Converter converter) {
        if (converter != null) {
            return copy(source, target, camel, true, converter);
        } else {
            return copy(source, target, camel, false, null);
        }
    }

    public static <K, T> T copy(K source, Class<T> target, boolean camel, boolean useConvert) {
        return copy(source, target, camel, useConvert, DEFAULT_CONVERTER);
    }

    @SuppressWarnings("unchecked")
    public static <K, T> T copy(K source, Class<T> target, boolean camel, boolean useConvert, Converter converter) {
        if (Map.class.isAssignableFrom(target)) {
            return toMap(source, target, camel ? QuickBeanMapWrapper.ParseType.TO_CAMEL : QuickBeanMapWrapper.ParseType.NONE, converter);
        }

        QuickExtendBeanCopier beanCopier = loadBeanCopier(source.getClass(), target, camel, useConvert);
        T res = (T) ReflectUtils.newInstance(target);
        beanCopier.copy(source, res, converter);
        return res;
    }

    private static QuickExtendBeanCopier loadBeanCopier(Class source, Class target, boolean camel, boolean useConvert) {
        BeanCopierOptions pair = new BeanCopierOptions(source, target, camel, useConvert);
        QuickExtendBeanCopier beanCopier = beanCopierCache.get(pair);
        if (beanCopier == null) {
            synchronized (beanCopierCache) {
                beanCopier = beanCopierCache.get(pair);
                if (beanCopier == null) {
                    beanCopier = QuickExtendBeanCopier.create(source, target, useConvert, camel);
                    beanCopierCache.put(pair, beanCopier);
                }
            }
        }
        return beanCopier;
    }

    public static <K, T> T toMap(K source, Class<T> target) {
        return toMap(source, target, QuickBeanMapWrapper.ParseType.NONE);
    }

    public static <K, T> T toMap(K source, Class<T> target, QuickBeanMapWrapper.ParseType parseType) {
        return toMap(source, target, parseType, DEFAULT_CONVERTER);
    }

    @SuppressWarnings("unchecked")
    public static <K, T> T toMap(K source, Class<T> target, QuickBeanMapWrapper.ParseType parseType, Converter converter) {
        return (T) QuickBeanMapWrapper.bean2map(source, target, parseType, converter);
    }
}
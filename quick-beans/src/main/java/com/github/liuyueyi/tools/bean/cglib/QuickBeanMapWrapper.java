package com.github.liuyueyi.tools.bean.cglib;

import com.github.liuyueyi.tools.core.StrUtil;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;
import net.sf.cglib.core.ReflectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class QuickBeanMapWrapper {
    public enum ParseType {
        NONE {
            @Override
            Object parseKey(Object key) {
                return key;
            }
        },
        TO_CAMEL {
            @Override
            Object parseKey(Object key) {
                if (key instanceof String) {
                    return StrUtil.toCamelCase((String) key);
                }
                return key;
            }
        },
        TO_UNDER_LINE {
            @Override
            Object parseKey(Object key) {
                if (key instanceof String) {
                    return StrUtil.toUnderCase((String) key);
                }
                return key;
            }
        };

        public void parse(Object key, Object value, Converter converter, Map target) {
            value = ParseType.convert(value, converter);
            target.put(parseKey(key), value);
        }

        public static Object convert(Object value, Converter converter) {
            if (converter == null) {
                return value;
            }
            value = converter.convert(value, null, null);
            return value;
        }

        abstract Object parseKey(Object key);
    }

    public static <K> Map bean2map(K source, Class mapClz, ParseType parseType, Converter converter) {
        BeanMap beanMap = BeanMap.create(source);
        Map res;
        if (mapClz == Map.class) {
            if (parseType == ParseType.NONE && converter == null) {
                // 不做任何处理，直接返回
                return beanMap;
            }

            res = new HashMap<>();
        } else {
            res = (Map) ReflectUtils.newInstance(mapClz);
        }

        // 做一个适配并返回
        for (Object key : beanMap.keySet()) {
            parseType.parse(key, beanMap.get(key), converter, res);
        }
        return res;
    }
}

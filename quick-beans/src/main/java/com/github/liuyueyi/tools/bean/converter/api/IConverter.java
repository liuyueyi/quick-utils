package com.github.liuyueyi.tools.bean.converter.api;

/**
 * @author yihui
 * @date 2021/4/9
 */
public interface IConverter extends Comparable {
    int order();

    boolean enable(Class target);

    Object convert(Object value, Class target);
}

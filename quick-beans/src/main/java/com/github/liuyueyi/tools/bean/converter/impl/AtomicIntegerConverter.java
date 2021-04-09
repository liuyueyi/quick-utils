package com.github.liuyueyi.tools.bean.converter.impl;

import com.github.liuyueyi.tools.bean.converter.api.BasicConverter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class AtomicIntegerConverter extends BasicConverter {
    @Override
    protected Object convertInternal(Object input) {
        if (input instanceof Integer) {
            return new AtomicInteger((Integer) input);
        }

        return new AtomicInteger(Integer.parseInt(convertToStr(input)));
    }

    @Override
    public boolean enable(Class target) {
        return target == AtomicInteger.class;
    }
}

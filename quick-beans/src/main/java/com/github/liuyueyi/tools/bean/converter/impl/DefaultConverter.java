package com.github.liuyueyi.tools.bean.converter.impl;

import com.github.liuyueyi.tools.bean.converter.api.BasicConverter;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class DefaultConverter extends BasicConverter {
    @Override
    protected Object convertInternal(Object input) {
        return input;
    }

    @Override
    public boolean enable(Class target) {
        return true;
    }
}

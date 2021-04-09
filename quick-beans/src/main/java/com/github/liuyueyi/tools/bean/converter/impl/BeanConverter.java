package com.github.liuyueyi.tools.bean.converter.impl;

import com.github.liuyueyi.tools.bean.BeanUtils;
import com.github.liuyueyi.tools.bean.converter.ComposeConverter;
import com.github.liuyueyi.tools.bean.converter.api.BasicConverter;

/**
 * 嵌套bean转换
 *
 * @author yihui
 * @date 2021/4/9
 */
public class BeanConverter extends BasicConverter {
    @Override
    protected Object convertInternal(Object input, Class target) {
        if (rootConverter instanceof ComposeConverter) {
            return BeanUtils.copy(input, target, ((ComposeConverter) rootConverter).isCamelEnable(), rootConverter);
        }
        return BeanUtils.copy(input, target, rootConverter);
    }

    @Override
    protected Object convertInternal(Object input) {
        return null;
    }

    @Override
    public boolean enable(Class target) {
        return true;
    }

    @Override
    public int order() {
        return 100;
    }
}

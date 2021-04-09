package com.github.liuyueyi.tools.bean.converter.api;


import com.github.liuyueyi.tools.core.ArrayUtil;
import com.github.liuyueyi.tools.core.CharUtil;
import net.sf.cglib.core.Converter;

import java.util.Map;

/**
 * @author yihui
 * @date 2021/4/9
 */
public abstract class BasicConverter implements IConverter {
    protected Converter rootConverter;

    public void setRootConverter(Converter rootConverter) {
        this.rootConverter = rootConverter;
    }

    @Override
    public Object convert(Object value, Class target) {
        if (target == null || value.getClass() == target) {
            return value;
        }

        if (target.isInstance(value) && !Map.class.isAssignableFrom(target)) {
            return target.cast(value);
        } else {
            return this.convertInternal(value, target);
        }
    }

    protected String convertToStr(Object value) {
        if (null == value) {
            return null;
        } else if (value instanceof CharSequence) {
            return value.toString();
        } else if (value.getClass().isArray()) {
            return ArrayUtil.toString(value);
        } else {
            return CharUtil.isChar(value) ? CharUtil.toString((Character) value) : value.toString();
        }
    }

    /**
     * 对象转换
     *
     * @param input
     * @return
     */
    protected Object convertInternal(Object input, Class target) {
        return convertInternal(input);
    }

    protected abstract Object convertInternal(Object input);

    @Override
    public int order() {
        return 10;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof IConverter)) {
            return 0;
        }

        if (((IConverter) o).order() > order()) {
            return -1;
        } else {
            return 1;
        }
    }
}

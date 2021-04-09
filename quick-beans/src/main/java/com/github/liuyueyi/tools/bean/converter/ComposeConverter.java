package com.github.liuyueyi.tools.bean.converter;

import com.github.liuyueyi.tools.bean.converter.api.IConverter;
import com.github.liuyueyi.tools.bean.converter.impl.DefaultConverter;
import net.sf.cglib.core.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class ComposeConverter implements Converter {
    private IConverter DEFAULT_CONVERTER = new DefaultConverter();

    private boolean camelEnable;
    private List<IConverter> converterList = new ArrayList<>();

    public ComposeConverter addConverter(IConverter converter) {
        converterList.add(converter);
        converterList.sort(null);
        return this;
    }

    public ComposeConverter camelEnable(boolean camelEnable) {
        this.camelEnable = camelEnable;
        return this;
    }

    public boolean isCamelEnable() {
        return camelEnable;
    }

    @Override
    public Object convert(Object value, Class target, Object context) {
        for (IConverter converter : converterList) {
            if (converter.enable(target)) {
                return converter.convert(value, target);
            }
        }

        return DEFAULT_CONVERTER.convert(value, target);
    }
}

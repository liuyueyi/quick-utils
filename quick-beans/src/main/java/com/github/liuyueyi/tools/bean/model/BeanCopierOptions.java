package com.github.liuyueyi.tools.bean.model;

import java.util.Objects;

/**
 * @author yihui
 * @date 2021/4/9
 */
public final class BeanCopierOptions {
    private Class source;
    private Class target;
    private boolean camel;
    private boolean covert;

    public BeanCopierOptions(Class source, Class target, boolean camel, boolean covert) {
        this.source = source;
        this.target = target;
        this.camel = camel;
        this.covert = covert;
    }

    public Class getSource() {
        return source;
    }

    public void setSource(Class source) {
        this.source = source;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public boolean isCamel() {
        return camel;
    }

    public void setCamel(boolean camel) {
        this.camel = camel;
    }

    public boolean isCovert() {
        return covert;
    }

    public void setCovert(boolean covert) {
        this.covert = covert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanCopierOptions that = (BeanCopierOptions) o;
        return camel == that.camel &&
                covert == that.covert &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, camel, covert);
    }

    @Override
    public String toString() {
        return "BeanCopierOptions{" +
                "source=" + source +
                ", target=" + target +
                ", camel=" + camel +
                ", covert=" + covert +
                '}';
    }
}

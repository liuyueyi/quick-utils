package com.github.liuyueyi.tools.bean.cglib;

import com.github.liuyueyi.tools.core.StrUtil;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.*;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

/**
 * cglib bean copier 扩展，支持驼峰与下划线的转换
 * <p>
 * - 也可以考虑针对 BulkBean 进行扩展，实现驼峰与下划线的互转
 *
 * @author yihui
 * @date 2021/4/8
 */
public abstract class QuickExtendBeanCopier {
    private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey) KeyFactory.create(BeanCopierKey.class);
    private static final Type CONVERTER =
            TypeUtils.parseType("net.sf.cglib.core.Converter");
    private static final Type BEAN_COPIER =
            TypeUtils.parseType("com.github.liuyueyi.tools.bean.cglib.QuickExtendBeanCopier");
    private static final Signature COPY =
            new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
    private static final Signature CONVERT =
            TypeUtils.parseSignature("Object convert(Object, Class, Object)");

    interface BeanCopierKey {
        Object newInstance(String source, String target, boolean useConverter, boolean camelEnable);
    }

    public static QuickExtendBeanCopier create(Class source, Class target, boolean useConverter, boolean camelEnable) {
        return new QuickExtendBeanCopier.Generator().setSource(source).setTarget(target)
                .setUseConverter(useConverter).setCamelEnable(camelEnable)
                .create();
    }

    abstract public void copy(Object from, Object to, Converter converter);

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE = new Source(BeanCopier.class.getName());
        private Class source;
        private Class target;
        private boolean useConverter;
        /**
         * 驼峰/下划线互转
         */
        private boolean camelEnable;

        public Generator() {
            super(SOURCE);
        }

        public Generator setSource(Class source) {
            if (!Modifier.isPublic(source.getModifiers())) {
                setNamePrefix(source.getName());
            }
            this.source = source;
            return this;
        }

        public Generator setTarget(Class target) {
            if (!Modifier.isPublic(target.getModifiers())) {
                setNamePrefix(target.getName());
            }

            this.target = target;
            return this;
        }

        public Generator setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
            return this;
        }

        public Generator setCamelEnable(boolean camelEnable) {
            this.camelEnable = camelEnable;
            return this;
        }

        @Override
        protected ClassLoader getDefaultClassLoader() {
            return source.getClassLoader();
        }

        @Override
        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(source);
        }

        public QuickExtendBeanCopier create() {
            // 缓存key，避免每次调用都重新生成代理类
            Object key = KEY_FACTORY.newInstance(source.getName(), target.getName(), useConverter, camelEnable);
            return (QuickExtendBeanCopier) super.create(key);
        }

        @Override
        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType(source);
            Type targetType = Type.getType(target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(Constants.V1_7,
                    Constants.ACC_PUBLIC,
                    getClassName(),
                    BEAN_COPIER,
                    null,
                    Constants.SOURCE_FILE);

            EmitUtils.null_constructor(ce);
            CodeEmitter codeEmitter = ce.begin_method(Constants.ACC_PUBLIC, COPY, null);
            PropertyDescriptor[] setters = ReflectUtils.getBeanSetters(target);

            Map<String, PropertyDescriptor> names = this.buildGetterNameMapper(source);
            Local targetLocal = codeEmitter.make_local();
            Local sourceLocal = codeEmitter.make_local();
            if (useConverter) {
                codeEmitter.load_arg(1);
                codeEmitter.checkcast(targetType);
                codeEmitter.store_local(targetLocal);
                codeEmitter.load_arg(0);
                codeEmitter.checkcast(sourceType);
                codeEmitter.store_local(sourceLocal);
            } else {
                codeEmitter.load_arg(1);
                codeEmitter.checkcast(targetType);
                codeEmitter.load_arg(0);
                codeEmitter.checkcast(sourceType);
            }
            for (int i = 0; i < setters.length; i++) {
                PropertyDescriptor setter = setters[i];
                PropertyDescriptor getter = this.loadSourceGetter(names, setter);
                if (getter != null) {
                    MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                    MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                    if (useConverter) {
                        Type setterType = write.getSignature().getArgumentTypes()[0];
                        codeEmitter.load_local(targetLocal);
                        codeEmitter.load_arg(2);
                        codeEmitter.load_local(sourceLocal);
                        codeEmitter.invoke(read);
                        codeEmitter.box(read.getSignature().getReturnType());
                        EmitUtils.load_class(codeEmitter, setterType);
                        codeEmitter.push(write.getSignature().getName());
                        codeEmitter.invoke_interface(CONVERTER, CONVERT);
                        codeEmitter.unbox_or_zero(setterType);
                        codeEmitter.invoke(write);
                    } else if (compatible(getter, setter)) {
                        codeEmitter.dup2();
                        codeEmitter.invoke(read);
                        codeEmitter.invoke(write);
                    }
                }
            }
            codeEmitter.return_value();
            codeEmitter.end_method();
            ce.end_class();
        }

        private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
            // TODO: allow automatic widening conversions?
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
        }

        @Override
        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        @Override
        protected Object nextInstance(Object instance) {
            return instance;
        }

        /**
         * 获取目标的getter方法，支持下划线与驼峰
         *
         * @param source
         * @return
         */
        public Map<String, PropertyDescriptor> buildGetterNameMapper(Class source) {
            PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(source);
            Map<String, PropertyDescriptor> names = new HashMap<>(getters.length);
            for (int i = 0; i < getters.length; ++i) {
                String name = getters[i].getName();
                names.put(name, getters[i]);
                if (!camelEnable) {
                    continue;
                }

                String camelName = StrUtil.toCamelCase(name);
                if (!name.equalsIgnoreCase(camelName)) {
                    // 支持下划线转驼峰
                    names.put(camelName, getters[i]);
                }
            }
            return names;
        }

        /**
         * 根据target的setter方法，找到source的getter方法，支持下划线与驼峰的转换
         *
         * @param names
         * @param setter
         * @return
         */
        public PropertyDescriptor loadSourceGetter(Map<String, PropertyDescriptor> names, PropertyDescriptor setter) {
            String setterName = setter.getName();
            PropertyDescriptor descriptor = names.get(setterName);
            if (!camelEnable) {
                return descriptor;
            }

            if (descriptor == null) {
                return names.get(StrUtil.toCamelCase(setterName));
            }
            return descriptor;
        }
    }
}

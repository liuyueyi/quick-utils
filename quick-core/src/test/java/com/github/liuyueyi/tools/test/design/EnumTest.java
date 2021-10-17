package com.github.liuyueyi.tools.test.design;

import org.junit.Test;

import java.util.stream.Stream;

/**
 * @author yihui
 * @date 2021/8/15
 */
public class EnumTest {

    public enum SingleEnum {
        INSTANCE;

        public void print(String word) {
            System.out.println(word);
        }
    }

    @Test
    public void testSingle() {
        SingleEnum.INSTANCE.print("hello world");
    }

    public enum SaveStrategyEnum {
        DB("db") {
            @Override
            public void save(Object obj) {
                System.out.println("save in db:" + obj);
            }
        },
        FILE("file") {
            @Override
            public void save(Object obj) {
                System.out.println("save in file: " + obj);
            }
        },
        OSS("oss") {
            @Override
            public void save(Object obj) {
                System.out.println("save in oss: " + obj);
            }
        };

        private String type;

        SaveStrategyEnum(String type) {
            this.type = type;
        }

        public abstract void save(Object obj);

        public static SaveStrategyEnum typeOf(String type) {
            for (SaveStrategyEnum strategyEnum: values()) {
                if (strategyEnum.type.equalsIgnoreCase(type)) {
                    return strategyEnum;
                }
            }
            return null;
        }
    }

    public void save(String type, Object data) {
        SaveStrategyEnum strategyEnum = SaveStrategyEnum.typeOf(type);
        if (strategyEnum != null) {
            strategyEnum.save(data);
        }
    }

    @Test
    public void testStrategy() {
        String type = "file".trim();

    }
}

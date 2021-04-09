package com.github.liuyueyi.tools.core;

/**
 * @author yihui
 * @date 2021/4/9
 */
public class CharUtil {
    public static class ASCIIStrCache {
        private static final int ASCII_LENGTH = 128;
        private static final String[] CACHE = new String[ASCII_LENGTH];

        public ASCIIStrCache() {
        }

        public static String toString(char c) {
            return c < ASCII_LENGTH ? CACHE[c] : String.valueOf(c);
        }

        static {
            for (char c = 0; c < 128; ++c) {
                CACHE[c] = String.valueOf(c);
            }
        }
    }

    public static boolean isChar(Object value) {
        return value instanceof Character || value.getClass() == Character.TYPE;
    }

    public static String toString(char c) {
        return ASCIIStrCache.toString(c);
    }
}

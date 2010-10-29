package com.mattinsler.cement.util;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 18, 2010
 * Time: 2:20:41 PM
 * To change this template use File | Settings | File Templates.
 */
public final class StringUtil {
    public static <T> String join(Iterable<T> iterable, String separator) {
        return join(iterable, separator, new Function<String, T>() {
            public String execute(T argument) {
                return argument.toString();
            }
        });
    }

    public static <T> String join(T[] array, String separator) {
        return join(array, separator, new Function<String, T>() {
            public String execute(T argument) {
                return argument.toString();
            }
        });
    }

    public static <T> String join(Iterable<T> iterable, String separator, Function<String, T> transform) {
        StringBuilder builder = new StringBuilder();
        if (iterable != null) {
            for (T item : iterable) {
                if (builder.length() > 0) {
                    builder.append(separator);
                }
                builder.append(transform.execute(item));
            }
        }
        return builder.toString();
    }

    public static <T> String join(T[] array, String separator, Function<String, T> transform) {
        StringBuilder builder = new StringBuilder();
        if (array != null) {
            for (T item : array) {
                if (builder.length() > 0) {
                    builder.append(separator);
                }
                builder.append(transform.execute(item));
            }
        }
        return builder.toString();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || "".equals(value);
    }
}

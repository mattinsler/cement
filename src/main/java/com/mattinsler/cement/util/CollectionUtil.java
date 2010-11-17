package com.mattinsler.cement.util;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 24, 2010
 * Time: 2:07:28 AM
 * To change this template use File | Settings | File Templates.
 */
public final class CollectionUtil {
    public static <T> List<T> collect(Iterator<T> iterator) {
        List<T> list = new ArrayList<T>();

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        return list;
    }

    public static <T> List<T> collect(Iterable<T> iterable) {
        return collect(iterable.iterator());
    }

    public static <T> List<T> collect(T[] array) {
        return Arrays.asList(array);
    }

    public static <T> boolean contains(Iterable<T> iterable, T item) {
        for (T t : iterable) {
            if (t.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(T[] array, T item) {
        for (T t : array) {
            if (t.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(Iterator<T> iterator, Function<Boolean, T> predicate) {
        while (iterator.hasNext()) {
            if (predicate.execute(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(Collection<T> collection, Function<Boolean, T> predicate) {
        return contains(collection.iterator(), predicate);
    }

    public static <T> boolean contains(T[] array, Function<Boolean, T> predicate) {
        for (T item : array) {
            if (predicate.execute(item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> int count(Iterator<T> iterator, Function<Boolean, T> predicate) {
        int count = 0;
        while (iterator.hasNext()) {
            if (predicate.execute(iterator.next())) {
                ++count;
            }
        }
        return count;
    }

    public static <T> int count(Collection<T> collection, Function<Boolean, T> predicate) {
        return count(collection.iterator(), predicate);
    }

    public static <T> int count(T[] array, Function<Boolean, T> predicate) {
        int count = 0;
        for (T item : array) {
            if (predicate.execute(item)) {
                ++count;
            }
        }
        return count;
    }
}

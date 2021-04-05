package io.github.superqtqt.aider.collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author superqtqt 2021-06-06
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionAider {
    /**
     * 判断当前是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 取两个集合的差集
     *
     * @param source
     * @param target
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T extends Collection<E>, E> List<E> diff(T source, T target) {
        List<E> result = new ArrayList<>();
        Set<E> set = new HashSet<>(target);
        source.forEach(item -> {
            if (!set.contains(item)) result.add(item);
        });

        return result;
    }

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static boolean isNullOrEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }
}

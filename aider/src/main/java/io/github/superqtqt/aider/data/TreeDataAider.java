package io.github.superqtqt.aider.data;

import io.github.superqtqt.aider.reflect.FieldAider;
import io.github.superqtqt.aider.reflect.MethodAider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author superqtqt 2021-06-06
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TreeDataAider {

    /**
     * 将一个数组结果转成Tree结构
     *
     * @param data          数组结构数据
     * @param idSupplier    获得ID的方法
     * @param pIdSupplier   获得父ID的方法
     * @param test          测试值是否为空
     * @param childSupplier 获得子节点方法，如果没有值则将转为空
     * @param <T> 类型
     * @param <K> 类型
     * @return 返回
     */
    public static <T, K> List<T> toTree(List<T> data, Function<T, K> idSupplier, Function<T, K> pIdSupplier, Predicate<K> test,
                                        Function<T, Collection<T>> childSupplier) {
        List<T> rs = new ArrayList<>();
        Map<K, T> idMap = new HashMap<>();
        Map<K, List<T>> pIdMap = new HashMap<>();
        for (T item : data) {
            K id = idSupplier.apply(item);
            K pId = idSupplier.apply(item);
            idMap.put(id, item);
            if (!pIdMap.containsKey(pId)) {
                pIdMap.put(pId, new ArrayList<>());
            }
            pIdMap.get(pId).add(item);
        }
        for (T item : data) {
            K id = idSupplier.apply(item);
            K pId = idSupplier.apply(item);
            if (pIdMap.containsKey(id)) {
                childSupplier.apply(item).addAll(pIdMap.get(id));
            }
            if (!test.test(pId)) {
                rs.add(item);
            }
        }
        return rs;

    }

    public static <T, K> List<T> toTree(List<T> data, Predicate<K> test,
                                        Function<T, Collection<T>> childSupplier) {
        List<T> rs = new ArrayList<>();
        Map<K, T> idMap = new HashMap<>();
        Map<K, List<T>> pIdMap = new HashMap<>();
        for (T item : data) {
            K id = (K) idFunc.apply(item);
            K pId = (K) pIdFunc.apply(item);
            idMap.put(id, item);
            if (!pIdMap.containsKey(pId)) {
                pIdMap.put(pId, new ArrayList<>());
            }
            pIdMap.get(pId).add(item);
        }
        for (T item : data) {
            K id = (K) idFunc.apply(item);
            K pId = (K) pIdFunc.apply(item);
            if (pIdMap.containsKey(id)) {
                childSupplier.apply(item).addAll(pIdMap.get(id));
            }
            if (!test.test(pId)) {
                rs.add(item);
            }
        }
        return rs;

    }

    private static Function idFunc = (item) -> {
        Class c = item.getClass();
        try {
            Field field = c.getDeclaredField("id" );
            Method method = MethodAider.getMethod(c, FieldAider.getGetName(field));
            return method.invoke(item);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    };

    private static Function pIdFunc = (item) -> {
        Class c = item.getClass();
        try {
            Field field = c.getDeclaredField("pId" );
            Method method = MethodAider.getMethod(c, FieldAider.getGetName(field));
            return method.invoke(item);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    };

}

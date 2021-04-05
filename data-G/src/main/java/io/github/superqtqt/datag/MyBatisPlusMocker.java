package io.github.superqtqt.datag;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.mysql.cj.MysqlType;
import io.github.superqtqt.aider.bean.BeanAider;
import io.github.superqtqt.aider.reflect.FieldAider;
import io.github.superqtqt.aider.reflect.MethodAider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.Connection;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author superqtqt 2020/9/9
 */
@Slf4j
public class MyBatisPlusMocker extends Mocker {

    private JDBCMocker jdbcMocker;

    public MyBatisPlusMocker(JDBCMocker jdbcMocker) {
        this.jdbcMocker = jdbcMocker;
    }

    /**
     * 根据mybatis的注解和表信息生成数据
     *
     * @param c      实体类
     * @param con
     * @param config
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> List<T> mockMybatisTable(@Nonnull Class<T> c, @Nonnull Connection con,
                                        @Nonnull ConfigBuilder.Config config) {
        TableName tableNameA = c.getDeclaredAnnotation(TableName.class);
        String tableName = "";
        if (tableNameA == null || Strings.isNullOrEmpty(tableNameA.value())) {
            //如果没有类则用表名，然后下滑线分割
            tableName = toDBName(c.getSimpleName());
        } else {
            tableName = tableNameA.value();
        }

        Map<String, ColumnMeta> columnMet = jdbcMocker.getColumnMeta(tableName, con);
        Map<String, DataCreator> allCreator = new HashMap<>();

        Class currentC = c;
        List<ReflectNode> nodes = new ArrayList<>();
        int index = 0;
        while (currentC != null) {
            ReflectNode node = new ReflectNode();
            if (currentC.getGenericSuperclass() instanceof ParameterizedTypeImpl) {
                node.actualTypes = ((ParameterizedTypeImpl) currentC.getGenericSuperclass())
                        .getActualTypeArguments();
            }
            node.typeParams = currentC.getTypeParameters();
            nodes.add(node);
            for (Field declaredField : currentC.getDeclaredFields()) {
                final String filedName = declaredField.getName();
                if (config.getIgnore().contains(filedName) || declaredField.isSynthetic()) {
                    continue;
                }
                TableField tableField = declaredField.getAnnotation(TableField.class);
                String tableColumn = "";
                if (tableField == null || Strings.isNullOrEmpty(tableField.value())) {
                    tableColumn = toDBName(filedName);
                } else {
                    tableColumn = tableField.value().replaceAll("`", "");
                }

                ColumnMeta meta = columnMet.get(tableColumn);
                meta.setName(filedName);
                meta.setC(declaredField.getType());
                meta.setType(declaredField.getGenericType());

                if (findIndex(currentC.getTypeParameters(), declaredField.getGenericType()) != -1) {
                    //获得当前是第几个泛型，然后坐子类中拿出来
                    int fxIndex = findIndex(currentC.getTypeParameters(),
                            declaredField.getGenericType());
                    meta.setType(nodes.get(index - 1).actualTypes[fxIndex]);
                    meta.setC(Class.forName(meta.getType().getTypeName()));

                    DataCreator dataCreator = new DataCreator(meta,
                            (param) -> {
                                final MockConfig mockConfig = new MockConfig();
                                mockConfig.sizeRange(1, 1);
                                return JMockData.mock(param.getC(), mockConfig);
                            });
                    allCreator.put(filedName, dataCreator);
                } else if (config.getDataCreatorMap().containsKey(filedName)) {
                    DataCreator dataCreator = config.getDataCreatorMap().get(filedName);
                    dataCreator.setMeta(meta);
                    allCreator.put(filedName, dataCreator);
                } else if (isJDBCType(meta.getC())) {
                    allCreator.put(filedName, get(meta));
                } else if (meta.getC().isEnum()) {
                    DataCreator dataCreator = new DataCreator(meta,
                            (param) -> JMockData.mock(param.getC()));
                    allCreator.put(filedName, dataCreator);
                    //如果是泛型
                } else {
                    DataCreator dataCreator = new DataCreator(meta,
                            (param) -> {
                                final MockConfig mockConfig = new MockConfig();
                                mockConfig.sizeRange(1, 1);
                                return JMockData.mock(new TypeReference<Object>() {
                                    @Override
                                    public Type getType() {
                                        return param.getType();
                                    }
                                }, mockConfig);
                            });
                    allCreator.put(filedName, dataCreator);
                }
            }
            currentC = currentC.getSuperclass();
            index++;
        }

        List<T> rs = new ArrayList<>();
        for (int i = 0; i < config.getRow(); i++) {
            T t = c.newInstance();
            for (Entry<String, DataCreator> entry : allCreator.entrySet()) {
                Method method = MethodAider.getMethod(c, BeanAider.getSetName(entry.getKey()),
                        FieldAider.getField(c, entry.getKey()).getType());
                try {
                    DataCreator.DataCreatorFunc fuc = entry.getValue().getDataCreatorFunc();
                    fuc.create(entry.getValue().getMeta());
                    method.invoke(t, entry.getValue().getDataCreatorFunc()
                            .create(entry.getValue().getMeta()));
                } catch (Exception e) {
                    log.error("{} 在进行实体注入时报错,参数{}", method.getName());
                    log.error("", e);
                }

            }
            rs.add(t);
        }
        return rs;
    }

    private <T> int findIndex(T[] arr, T findOne) {
        if (arr == null || arr.length == 0) {
            return -1;
        } else {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].equals(findOne)) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * 判断是不是JDBC基础类型
     *
     * @param c
     * @return
     */
    private static boolean isJDBCType(Class c) {
        Set<String> types = Arrays.stream(MysqlType.values()).map(MysqlType::getClassName)
                .collect(Collectors.toSet());
        if (types.contains(c.getName())) {
            return true;
        }
        Set<Class> classTypes = Sets
                .newHashSet(int.class, long.class, double.class, float.class, boolean.class,
                        char.class);
        if (classTypes.contains(c)) {
            return true;
        }

        return false;
    }

    private static String toDBName(String fieldName) {
        StringBuilder name = new StringBuilder();
        char[] charArr = fieldName.toCharArray();
        for (int i = 0; i < charArr.length; i++) {
            char c = charArr[i];
            if (c == Character.toUpperCase(c)) {
                if (name.length() > 0) {
                    name.append("_");
                }
                for (int j = i; j < charArr.length; j++) {
                    if (charArr[j] != Character.toUpperCase(charArr[j])
                            || j == charArr.length - 1) {
                        //
                        while (i <= j) {
                            name.append(Character.toLowerCase(charArr[i]));
                            i++;
                        }
                        i--;
                        break;
                    }
                }
            } else {
                name.append(c);
            }
        }
        return name.toString();
    }


    private static class ReflectNode {

        Type[] actualTypes;
        TypeVariable[] typeParams;


    }


}

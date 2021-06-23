package io.github.superqtqt.aider.jdbc;

import io.github.superqtqt.aider.collection.CollectionAider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author superqtqt 2021-06-06
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JDBCAider {
    public static void close(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量插入数据
     *
     * @param con 连接
     * @param tableName    表名
     * @param columns      列名
     * @param data         数据
     * @param bathSize     一批插入量
     * @param dataOperator 从数据中读取每个字段的值
     * @param <T> 插入的数据
     */
    public static <T> void bathInsert(Connection con, String tableName, String[] columns, List<T> data, int bathSize, DataOperator<T> dataOperator) {
        StringBuilder sql = new StringBuilder("insert into " + tableName + "(");
        sql.append(Arrays.stream(columns).collect(Collectors.joining(",")));
        sql.append(") values ");
        final String valueIdentify = "(" + getValueIdentiy(columns.length) + ")";
        StringBuilder exeSQL = new StringBuilder(sql.toString());
        List<Object> params = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (i > 0 && i % bathSize == 0) {
                exeSQL(con, exeSQL.toString(), params);
                exeSQL = new StringBuilder(sql.toString());
                params = new ArrayList<>();
            } else {
                if (i % bathSize >= 1) {
                    exeSQL.append(",");
                }
                exeSQL.append(valueIdentify);
                for (int j = 0; j < columns.length; j++) {
                    params.add(dataOperator.get(data.get(i), j));
                }
            }
        }
        if (params.size() > 0) {
            exeSQL(con, exeSQL.toString(), params);
        }
    }

    @SneakyThrows
    private static boolean exeSQL(Connection con, String sql, List<Object> params) {
        log.debug("exe sql={}", sql);
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
        return ps.execute();
    }

    private static String getValueIdentiy(int len) {

        if (len == 0) {
            return "";
        } else if (len == 1) {
            return "?";
        }
        int index = 0;
        StringBuilder values = new StringBuilder();
        while (index < len) {
            if (index == 0) {
                values.append("?");
            } else {
                values.append(",?");
            }
            index++;
        }
        return values.toString();
    }

    @FunctionalInterface
    public interface DataOperator<T> {
        Object get(T t, int i);
    }

    @SneakyThrows
    public static ResultSet query(Connection con, String sql, Object[] params) {
        PreparedStatement ps = con.prepareStatement(sql);
        if (!CollectionAider.isNullOrEmpty(params)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
        return ps.executeQuery();
    }


    @SneakyThrows
    public static <T> List<T> query(Connection con, String sql, Object[] params, Function<ResultSet, T> func) {
        ResultSet rs = query(con, sql, params);
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(func.apply(rs));
        }
        return list;
    }

    public static Function<ResultSet, Map<String, Object>> toMap = resultSet -> {
        try {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            return map;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    @SneakyThrows
    public static <T> List<T> query(Connection con, String sql, Object[] params, Class<T> cl) {
        ResultSet rs = query(con, sql, params);
        List<T> list = new ArrayList<>();
        Map<String, Method> methodMap = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String name = metaData.getColumnName(i);
            String type = metaData.getColumnClassName(i);
            String methodName = toMethodName(name);
            Method method = cl.getMethod(methodName, Class.forName(type));
            methodMap.put(name, method);
        }
        while (rs.next()) {
            T instance = cl.newInstance();
            for (Map.Entry<String, Method> method : methodMap.entrySet()) {
                method.getValue().invoke(instance, rs.getObject(method.getKey()));
            }
            list.add(instance);
        }
        return list;
    }

    private static String toMethodName(String dbCloudName) {
        StringBuilder builder = new StringBuilder();
        builder.append("set");
        for (String s : dbCloudName.split("_")) {
            if (s.length() == 1) {
                builder.append(s);
            } else {
                builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
            }
        }
        return builder.toString();
    }


}

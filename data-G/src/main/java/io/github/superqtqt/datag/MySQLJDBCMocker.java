package io.github.superqtqt.datag;

import com.mysql.cj.MysqlType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author superqtqt 2020/9/9
 */
@Slf4j
public final class MySQLJDBCMocker extends JDBCMocker {

    @Override
    public List<Map<String, Object>> mockTable(@Nonnull String tableName,
            @Nonnull Connection con, @Nonnull ConfigBuilder.Config config) {
        Map<String, DataCreator> allCreator = new HashMap<>();
        Map<String, ColumnMeta> metaMap = getColumnMeta(tableName, con);
        for (Entry<String, ColumnMeta> entry : metaMap.entrySet()) {
            if (config.getIgnore().contains(entry.getKey())) {
                continue;
            } else if (config.getDataCreatorMap().containsKey(entry.getKey())) {
                DataCreator dataCreator = config.getDataCreatorMap().get(entry.getKey());
                dataCreator.setMeta(entry.getValue());
                allCreator.put(entry.getKey(), dataCreator);
            } else {
                allCreator.put(entry.getKey(), get(entry.getValue()));
            }
        }
        List<Map<String, Object>> rs = new ArrayList<>();
        for (int i = 0; i < config.getRow(); i++) {
            Map<String, Object> row = new HashMap<>();
            for (Entry<String, DataCreator> entry : allCreator.entrySet()) {
                row.put(entry.getKey(),
                        entry.getValue().getDataCreatorFunc().create(entry.getValue().getMeta()));
            }
            rs.add(row);
        }
        return rs;
    }

    @SneakyThrows
    @Override
    public Map<String, ColumnMeta> getColumnMeta(@Nonnull String tableName,
                                                                       @Nonnull Connection con) {
        DatabaseMetaData m_DBMetaData = con.getMetaData();
        Map<String, ColumnMeta> map = new HashMap<>();
        ResultSet colRet = m_DBMetaData.getColumns(null, "%", tableName, "%");
        while (colRet.next()) {
            final String columnName = colRet.getString("COLUMN_NAME");
            Integer len = colRet.getInt("COLUMN_SIZE");
            Integer digit = colRet.getInt("DECIMAL_DIGITS");
            ColumnMeta columnMeta = new ColumnMeta();
            columnMeta.setDigit(digit);
            columnMeta.setMaxLen(len == null ? 6 : len);
            map.put(columnName, columnMeta);
        }
        final String sql = "select * from " + tableName + " where 1=2";
        ResultSet rs = con.createStatement().executeQuery(sql);
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            int j = i + 1;
            final String columnName = rs.getMetaData().getColumnName(j);
            MysqlType type = MysqlType.getByName(rs.getMetaData().getColumnTypeName(j));
            ColumnMeta columnMeta = map.get(columnName);
            columnMeta.setC(type.getClassName().equals("[B")
                    ? null
                    : Class.forName(type.getClassName()));
            switch (type) {
                case INT_UNSIGNED:
                case TINYINT_UNSIGNED:
                case SMALLINT_UNSIGNED:
                case MEDIUMINT_UNSIGNED:
                case BIGINT_UNSIGNED:
                case DECIMAL_UNSIGNED:
                case FLOAT_UNSIGNED:
                case DOUBLE_UNSIGNED: {
                    columnMeta.setIsPositive(true);
                    break;
                }
                default: {
                    continue;
                }
            }
        }
        return map;
    }
}
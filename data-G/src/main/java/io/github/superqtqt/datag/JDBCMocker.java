package io.github.superqtqt.datag;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**

 *
 * @author superqtqt 2020/9/9
 */
@Slf4j
public abstract class JDBCMocker extends Mocker{

    public abstract List<Map<String, Object>> mockTable(@Nonnull final String tableName,
            @Nonnull Connection con,
            @Nonnull ConfigBuilder.Config config);

    public abstract Map<String,ColumnMeta> getColumnMeta(@Nonnull final String tableName,@Nonnull Connection con);

    /**
     * 将数据插入到库中
     *
     * @param tableName
     * @param con
     * @param rs
     * @return
     */
    @SneakyThrows
    public static int mockDataToDb(@Nonnull final String tableName,
            @Nonnull Connection con, @Nonnull List<Map<String, Object>> rs) {
        PreparedStatement ps = null;
        List<String> header = new ArrayList<>(), values = new ArrayList<>();
        for (Map<String, Object> r : rs) {
            if (header.size() == 0) {
                for (Entry<String, Object> stringObjectEntry : r.entrySet()) {
                    header.add(stringObjectEntry.getKey());
                    values.add("?");
                }
                final String sql =
                        "insert into " + tableName + " (" + header.stream()
                                .collect(Collectors.joining(","))
                                + ") values (" + values.stream().collect(Collectors.joining(","))
                                + " )";
                ps = con.prepareStatement(sql);
            }
            int index = 1;
            for (Entry<String, Object> entry : r.entrySet()) {
                ps.setObject(index, entry.getValue());
                index++;
            }
            ps.addBatch();
        }
        return ps.executeBatch().length;
    }

}

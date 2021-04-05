package io.github.superqtqt.datag;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author superqtqt 2020/9/9
 */
@Getter
public class DataCreator {

    @Setter
    private ColumnMeta meta;
    private DataCreatorFunc dataCreatorFunc;

    public DataCreator(ColumnMeta meta,
                       DataCreatorFunc dataCreatorFunc) {
        this.meta = meta;
        this.dataCreatorFunc = dataCreatorFunc;
    }

    /**
     * 生成数据的函数
     */
    @FunctionalInterface
    public interface DataCreatorFunc {

        Object create(ColumnMeta meta);
    }

}

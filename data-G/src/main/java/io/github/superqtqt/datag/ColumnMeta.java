package io.github.superqtqt.datag;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

/**
 *
 * @author superqtqt 2020/9/9
 */
@Getter
@Setter
public class ColumnMeta {
    private String name;
    private Type type;
    private Class c;
    private Integer maxLen;
    private Integer digit;
    //是不是正数
    private Boolean isPositive;


}

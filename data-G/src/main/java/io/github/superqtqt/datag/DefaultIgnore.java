package io.github.superqtqt.datag;

import com.google.common.collect.Lists;

import java.util.List;

/** 默认要忽略的信息

 *
 * @author superqtqt 2020/9/7
 */

public final class DefaultIgnore {

    /**
     * 根据类mock变量时要忽略的
     */
    public static final List<String> CLASS_IGNORE= Lists.newArrayList("logger","log","LOGGER","LOG","mapper","MAPPER");

}

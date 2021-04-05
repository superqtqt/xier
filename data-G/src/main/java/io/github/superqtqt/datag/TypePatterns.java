package io.github.superqtqt.datag;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**

 *
 * @author superqtqt 2020/9/7
 */
@Getter
public enum TypePatterns {
    _int(int.class, "\\d{%s,%s}"),
    _integer(Integer.class, "\\d{%s,%s}");

    private Class c;
    private String patterns;

    TypePatterns(Class c, String patterns) {
        this.c = c;
        this.patterns = patterns;
    }

    public static Map<Class, String> getAll() {
        Map<Class, String> map = new HashMap<>();
        for (TypePatterns value : TypePatterns.values()) {
            map.put(value.getC(), value.getPatterns());
        }
        return map;
    }

}

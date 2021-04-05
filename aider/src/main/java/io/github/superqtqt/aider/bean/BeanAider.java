package io.github.superqtqt.aider.bean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

/**
 * @author superqtqt 2021-06-06
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanAider {
    public static String getGetName(@Nonnull final String name) {
        return "get" + upFirstWor(name);
    }

    public static String getSetName(@Nonnull final String name) {
        return "set" + upFirstWor(name);
    }

    private static String upFirstWor(@Nonnull final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static <T extends Enum<T>> T convert(Enum name, Class<T> targetType) {
        String strName = name.toString();
        try {
            T[] arr = (T[]) targetType.getDeclaredMethod("values").invoke(targetType, null);
            for (T o : arr) {
                if (o.toString().equals(strName)) {
                    return o;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

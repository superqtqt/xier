package io.github.superqtqt.aider.reflect;

import com.google.common.base.Strings;
import io.github.superqtqt.aider.operation.OptAider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author superqtqt 2021/5/1
 */
@Slf4j
public final class FieldAider {

    public static Field getField(Class c, String name) {
        Class currentC = c;
        while (currentC != null) {
            try {
                return currentC.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                currentC = currentC.getSuperclass();
            }
        }
        return null;
    }

    public static String getGetName(Field field) {
        if (OptAider.in(field.getType(), Boolean.class, boolean.class)) {
            return "is"+upFirstWord(field.getName());
        }else {
            return "get"+upFirstWord(field.getName());
        }
    }

    static String upFirstWord(String name) {
        if (Strings.isNullOrEmpty(name) || name.length() <= 1) {
            return name;
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }
}

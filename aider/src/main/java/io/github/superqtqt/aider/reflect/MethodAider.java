package io.github.superqtqt.aider.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**

 *
 * @author superqtqt 2020/9/9
 */
@Slf4j
public final class MethodAider {

    public static Method getMethod(Class c,String name, Class<?>... parameterTypes){
        Class currentC=c;
        while (currentC!=null){
            try {
                return currentC.getDeclaredMethod(name,parameterTypes);
            } catch (NoSuchMethodException e) {
                currentC=currentC.getSuperclass();
            }
        }
        return null;
    }

}

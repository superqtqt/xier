package io.github.superqtqt.aider.operation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author superqtqt 2021-06-06
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptAider {

    public static boolean in(Object obj, Object... collections) {

        for (Object collection : collections) {
            if (obj == null) {
                if (collection == null) {
                    return true;
                }
            } else if (obj.equals(collection)) {
                return true;
            }
        }
        return false;
    }
}

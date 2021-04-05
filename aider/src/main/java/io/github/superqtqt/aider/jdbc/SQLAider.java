package io.github.superqtqt.aider.jdbc;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQLAider {

    public static List<String> split(String source, char splitChar) {
        if (Strings.isNullOrEmpty(source)) {
            return ImmutableList.of(source);
        }
        boolean hitSim = false;
        boolean hitDoubleSim = false;
        char escapeWord = '\\';
        List<String> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Character upChar = null;
        Character current=null;
        for (char c : source.toCharArray()) {
            current=c;
            if (c == '\'') {
                hitSim = !hitSim;
            } else if (c == '\"') {
                hitDoubleSim = !hitDoubleSim;
            }
            if (!hitSim && !hitDoubleSim && splitChar == c) {
                if (upChar == null) {
                    continue;
                } else if (c != escapeWord) {
                    list.add(sql.toString());
                    sql = new StringBuilder();
                    continue;
                }
            }
            sql.append(c);
            upChar = c;
        }
        if (current!=splitChar){
            list.add(sql.toString());
        }
        return list;
    }
}

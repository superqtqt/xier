package io.github.superqtqt.datag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** 构造一个配置项
 *
 * @author superqtqt 2020/9/9
 */
@Slf4j
public final class ConfigBuilder {

    public static Config build(){
        return new Config();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Config{
        private int row=1;
        private Set<String> ignore=new HashSet<>();

        private Map<String,DataCreator> dataCreatorMap=new HashMap<>();

        /**
         * 设置生成多少条记录
         * @param row
         * @return
         */
        public Config setRow(int row) {
            this.row = row;
            return this;
        }

        /**
         * 增加忽略的列
         * @param name
         * @return
         */
        public Config addIgnore(@Nonnull String name){
            ignore.add(name);
            return this;
        }

        /**
         * 增加忽略的列
         * @param names
         * @return
         */
        public Config addIgnores(@Nonnull Collection<String> names){
            ignore.addAll(names);
            return this;
        }

        /**
         * 预定义的数据构造方法
         * @param func
         * @return
         */
        public Config mockCreator(@Nonnull String name,@Nonnull DataCreator.DataCreatorFunc func){
            dataCreatorMap.put(name,new DataCreator(null,func));
            return this;
        }


    }


}

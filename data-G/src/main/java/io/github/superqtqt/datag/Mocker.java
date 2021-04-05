package io.github.superqtqt.datag;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import io.github.superqtqt.aider.json.JacksonAider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

/**

 *
 * @author superqtqt 2020/9/9
 */
@Slf4j
public abstract class Mocker {

    public DataCreator get(ColumnMeta meta) {
        if (meta.getC() == null) {
            throw new RuntimeException(String.format("列 %s 的Java类型为空，不支持生成数据;", meta.getName()));
        }
        final String cName = meta.getC().getName();
        final boolean isPositive = Boolean.TRUE.equals(meta.getIsPositive()) ? true : false;
        if (in(cName, int.class.getName(), Integer.class.getName(), BigInteger.class.getName())) {
            return new DataCreator(meta, (param) -> {
                Random random=new Random();
                int value = Double.valueOf(Math.pow(10, meta.getMaxLen())).intValue();
                random.ints(isPositive ? 0 : -1 * value, value);
                return random.nextInt();
            });
        } else if (in(cName, long.class.getName(), Long.class.getName())) {
            final MockConfig mockConfig = new MockConfig();
            int value = Double.valueOf(Math.pow(10, meta.getMaxLen())).intValue();
            mockConfig.longRange(isPositive ? 0 : -1 * value, value);
            return new DataCreator(meta, (param) -> JMockData.mock(Long.class, mockConfig));
        } else if (in(cName, BigDecimal.class.getName())) {
            final MockConfig mockConfig = new MockConfig();
            mockConfig.decimalScale(meta.getDigit());
            return new DataCreator(meta, (param) -> JMockData.mock(BigDecimal.class, mockConfig));
        } else if (in(cName, boolean.class.getName(), Boolean.class.getName())) {
            return new DataCreator(meta, (param) -> JMockData.mock(Boolean.class));
        } else if (in(cName, Float.class.getName())) {
            final MockConfig mockConfig = new MockConfig();
            Double v = Math.pow(10.0, meta.getMaxLen());
            mockConfig.floatRange(isPositive ? 0 : -1 * v.floatValue(), v.floatValue());
            return new DataCreator(meta, (param) -> JMockData.mock(float.class, mockConfig));
        } else if (in(cName, Double.class.getName())) {
            final MockConfig mockConfig = new MockConfig();
            Double v = Math.pow(10.0, meta.getMaxLen());
            mockConfig.doubleRange(isPositive ? 0 : -1 * v.doubleValue(), v.doubleValue());
            return new DataCreator(meta, (param) -> JMockData.mock(double.class, mockConfig));
        } else if (in(cName, Timestamp.class.getName(), Date.class.getName(),
                Time.class.getName())) {
            return new DataCreator(meta, (param) -> JMockData.mock(meta.getC()));
        } else if (in(cName, String.class.getName())) {
            MockConfig mockConfig = new MockConfig();
            int maxLen=meta.getMaxLen()>1000?1000:meta.getMaxLen();
            mockConfig.stringRegex("[a-zA-Z0-9]{0," + maxLen + "}");
            return new DataCreator(meta, (param) -> JMockData.mock(String.class, mockConfig));
        } else if (meta.getType() != null) {
            MockConfig mockConfig = new MockConfig();
            mockConfig.sizeRange(1,1);
            return new DataCreator(meta, (param) -> JMockData.mock(new TypeReference<Object>() {
                @Override
                public Type getType() {
                    return param.getType();
                }
            },mockConfig));
        }else {
            throw new RuntimeException("当前配置不支持:"+ JacksonAider.writeValueAsString(meta));
        }

    }

    private boolean in(String key, String... arr) {
        for (String s : arr) {
            if (key.equals(s)) {
                return true;
            }
        }
        return false;
    }

}

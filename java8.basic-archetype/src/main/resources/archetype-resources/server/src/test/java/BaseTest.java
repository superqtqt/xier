#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import io.github.superqtqt.datag.ConfigBuilder;
import io.github.superqtqt.datag.H2JDBCMocker;
import io.github.superqtqt.datag.MyBatisPlusMocker;
import ${package}.config.SwaggerConfig;
import io.github.superqtqt.aider.jdbc.JDBCAider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**

 *
 * @author superqtqt 2021/4/9
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(value = "${package}.**", lazyInit = true,excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {
                SwaggerConfig.class,${package}.App.class
        })
})
@EnableFeignClients(basePackages = {"${package}.**"})
@ActiveProfiles("h2test")
public class BaseTest {

    @Autowired
    public DataSource dataSource;

    protected ConfigBuilder.Config getDefConfig() {
        return ConfigBuilder.build().setRow(1)
                .addIgnore("logger")
                .addIgnore("log")
                .addIgnore("LOG")
                .addIgnore("LOGGER")
                .addIgnore("mapper")
                .addIgnore("MAPPER");
    }

    @SneakyThrows
    protected Connection getCon() {
        return dataSource.getConnection();
    }

    protected <T> List<T> mock(Class<T> c, ConfigBuilder.Config config) {
        MyBatisPlusMocker mocker = new MyBatisPlusMocker(new H2JDBCMocker());
        Connection con = getCon();
        try {
            return mocker.mockMybatisTable(c, con, config);
        } finally {
            JDBCAider.close(con);
        }
    }

    protected <T> T mock(Class<T> c) {
        MyBatisPlusMocker mocker = new MyBatisPlusMocker(new H2JDBCMocker());
        Connection con = getCon();
        try {
            return mocker.mockMybatisTable(c, con, getDefConfig()).get(0);
        } finally {
            JDBCAider.close(con);
        }
    }
}

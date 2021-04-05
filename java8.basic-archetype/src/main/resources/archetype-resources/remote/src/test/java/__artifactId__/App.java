#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**

 *
 * @author superqtqt 2020/7/31
 */
@SpringBootApplication
@ComponentScan("${package}")
@EnableFeignClients(basePackages={"${package}.**"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

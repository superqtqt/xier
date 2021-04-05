#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId};

import ${package}.BaseTest;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author superqtqt 2021-05-19
 */
public class DemoTest extends BaseTest
{
    @Autowired
    StringEncryptor encryptor;

    @Test
    public void getPass() {
        String url = encryptor.encrypt("jdbc:mysql://9.134.122.168:3306/dataops_circle?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&${artifactId}Timezone=GMT%2B8");
        String name = encryptor.encrypt("admin");
        String password = encryptor.encrypt("admin");
        System.out.println(url+"----------------");
        System.out.println(name+"----------------");
        System.out.println(password+"----------------");
    }
}

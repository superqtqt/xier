#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**

 *
 * @author superqtqt 2021/5/25
 */
@Slf4j
public class EMCTest extends BaseTest{
    @Autowired
    StringEncryptor encryptor;

    @Test
    public void test(){
        System.out.println(encryptor.encrypt("6Y1TrreWqHDJHVK9*()"));
    }
}

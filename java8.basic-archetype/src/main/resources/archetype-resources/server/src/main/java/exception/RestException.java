#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.exception;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**

 *
 * @author superqtqt 2021/4/9
 */
@Slf4j
public class RestException extends RuntimeException {
    public static final int ERROR_CODE = 1;

    @Getter
    protected int code = ERROR_CODE;
    @Getter
    protected String msg;

    public RestException() {
    }

    public RestException(String msg) {
        this.msg = msg;
    }

    public RestException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return Strings.isNullOrEmpty(super.getMessage()) ? this.msg : super.getMessage();
    }
}

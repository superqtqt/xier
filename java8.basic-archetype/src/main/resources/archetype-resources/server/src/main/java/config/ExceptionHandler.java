#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import io.github.superqtqt.module.bo.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**

 *
 * @author superqtqt 2021/4/12
 */
@Slf4j
@ControllerAdvice
public final class ExceptionHandler extends ResponseEntityExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResult globalException(Exception e)
    {
        log.error("业务异常: ", e);
        return RestResult.of(RestResult.ERROR_CODE, e.getMessage(), null);
    }


    /**
     * 校验消息转换拼接
     */
    private String msgConverter(BindingResult bindingResult)
    {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(fieldError -> sb.append(fieldError.getDefaultMessage()).append("; "));

        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    private String msgConverter(Set<ConstraintViolation<?>> constraintViolations)
    {
        StringBuilder sb = new StringBuilder();
        constraintViolations.forEach(violation -> sb.append(violation.getMessage()).append("; "));

        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request)
    {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            sb.append(fieldName).append(":").append(errorMessage).append(";");
        });

        RestResult resResult = RestResult.of(400, sb.toString(), null);
        return new ResponseEntity<>(resResult, headers, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request)
    {

        String errors = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL()
                + "请检查请求的 URL 和 METHOD 是否正确; Please check your request URL and METHOD.";

        RestResult resResult = RestResult.of(400, errors, null);
        return ResponseEntity.status(HttpStatus.OK).body(resResult);
    }

}

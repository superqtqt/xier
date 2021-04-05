package io.github.superqtqt.datag.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestResult<T> {
    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = 0;
    public static final String SUCCESS_MESSAGE = "成功";

    @ApiModelProperty(name = "返回编码", position = 0)
    @Getter
    @Setter
    private Integer code;
    @ApiModelProperty(name = "反馈信息", position = 1)
    @Getter
    @Setter
    private String message;
    @ApiModelProperty(name = "数据", position = 2)
    @Getter
    @Setter
    private T data;
    @ApiModelProperty(name = "调试信息", position = 3)
    @Getter
    @Setter
    private String debug;


    private RestResult(T data) {
        this.code = 0;
        this.message = "成功";
        this.data = data;
    }

    private RestResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> RestResult<T> ok(T data) {
        return new RestResult(data);
    }

    public static <T> RestResult<T> of(int code, String message, T data) {
        return new RestResult(code, message, data);
    }
}

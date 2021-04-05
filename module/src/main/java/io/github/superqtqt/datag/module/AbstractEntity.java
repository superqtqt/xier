package io.github.superqtqt.datag.module;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
public class AbstractEntity implements Serializable {

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        JacksonTypeHandler.setObjectMapper(objectMapper);
    }

    @ApiModelProperty(value = "创建人ID",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY, position = 990)
    @TableField(value = "created_by", updateStrategy = FieldStrategy.IGNORED)
    String createdBy;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", example = "2020-03-01 12:12:12",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY, position = 992)
    @TableField(value = "created_time", updateStrategy = FieldStrategy.IGNORED)
    LocalDateTime createTime = LocalDateTime.now();
    @ApiModelProperty(value = "更新人ID",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY, position = 993)
    @TableField("modified_by")
    String modifiedBy;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", example = "2020-03-01 12:12:12",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY, position = 995)
    @TableField("modified_time")
    LocalDateTime modifiedTime = LocalDateTime.now();
}

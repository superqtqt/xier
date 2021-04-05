#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId};

import com.google.common.base.Preconditions;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.function.Consumer;

/**

 *
 * @author superqtqt 2021/4/9
 */
@Slf4j
@AllArgsConstructor
public final class DefaultFeignClientInterceptor implements RequestInterceptor {
    private String serverName;
    private ServerType serverType;
    private Class<?> serviceClass;
    private Consumer<RequestTemplate> consumer;

    @Override
    public void apply(RequestTemplate template) {
        Preconditions.checkArgument(Strings.isNotEmpty(serverName), "serverName不能为空");
        Preconditions.checkArgument(Objects.nonNull(serverType), "serviceType不能为空");
        Preconditions.checkArgument(Objects.nonNull(serviceClass), "serviceClass不能为空");
        FeignClient annotation = serviceClass.getAnnotation(FeignClient.class);
        Preconditions.checkArgument(Objects.nonNull(annotation), "serviceClass缺少@FeignClient注解");
        UriComponents uriComponents;
        switch (serverType) {
            case domain:
                uriComponents = UriComponentsBuilder.newInstance()
                        .scheme("http").host(serverName).path(annotation.path())
                        .build();
                break;
            default:
                throw new RuntimeException(String.format("不支持的ServerType[%s]类型", serverType));
        }
        String url = uriComponents.encode().toUriString();
        if (consumer != null) {
            consumer.accept(template);
        }
        template.target(url);
    }

    public enum ServerType {
        domain;
    }

}

package ${package}.${artifactId}.demo;

import ${package}.${artifactId}.DefaultFeignClientInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superqtqt 2021-06-06
 */
@FeignClient(name = "usService", configuration = Demo.Configuration.class)
public interface Demo
{
    @RequestMapping(value = "/s", method = RequestMethod.GET)
    String searchBaidu(@RequestParam(name = "wd") String searchKey);


    class Configuration
    {

        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public RequestInterceptor feignClientInterceptor(
                @Value("${symbol_dollar}{demo.baidu.domain}") String serverName,
                @Value("${symbol_dollar}{demo.baidu.type:domain}") String serverType)
        {
            return new DefaultFeignClientInterceptor(serverName,
                    DefaultFeignClientInterceptor.ServerType.valueOf(serverType),
                    Demo.class, null);
        }

        @Bean
        Logger.Level feignLoggerLevel()
        {
            return Logger.Level.FULL;
        }

        @Bean
        public feign.Retryer retryer()
        {
            return new Retryer.Default(0, 50000, 3);
        }

        @Bean
        public Encoder formEncoder()
        {
            return new FormEncoder(new SpringEncoder(messageConverters));
        }

        @Bean
        public Decoder feignDecoder()
        {
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            List<MediaType> supportedMediaTypes = new ArrayList<>();
            supportedMediaTypes.add(MediaType.ALL);
            converter.setSupportedMediaTypes(supportedMediaTypes);
            ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(converter);
            return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
        }
    }
}

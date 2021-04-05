#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**

 *
 * @author superqtqt 2021/4/9
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Bean
//    public WebMvcConfigurer webMvcConfigurer() {
//        return new WebConfiguration();
//    }

    @Bean
    public Filter crossOrigin() {
        return (servletRequest, servletResponse, filterChain) -> {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Origin",
                    request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age","7200");
            filterChain.doFilter(servletRequest, servletResponse);
        };
    }

//    static class WebConfiguration implements WebMvcConfigurer {
//
//        @Override
//        public void addInterceptors(InterceptorRegistry registry) {
//            registry.addInterceptor(new LoginInterceptor())
//                    .addPathPatterns("/**")
//                    .excludePathPatterns("/error", "/*.xlsx", "/*.html", "/index.html*",
//                            "/manifest.json*", "/robots.txt*", "/webjars/**",
//                            "/swagger-resources/**",
//                            "/v2/api-docs-ext", "/precache-manifest.*", "/init/**").order(0);
//
//        }
//    }
}
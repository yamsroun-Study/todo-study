package jocture.todo.config;

import jocture.todo.web.argument.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// Enum : Java 1.5

@Configuration // @Component + 추가기능
public class WebMvcConfig implements WebMvcConfigurer {

    private static final int MAX_AGE_SECONDS = 3600; // 매직상수, 매직넘버

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            // .allowedHeaders("*")
            // .allowCredentials(true)
            .maxAge(MAX_AGE_SECONDS);
    }

    // 직접 만든 ArgumentResolver를 스프링에 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}

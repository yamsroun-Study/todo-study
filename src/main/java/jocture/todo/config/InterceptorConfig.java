package jocture.todo.config;

import jocture.todo.web.interceptor.AuthenticationCheckInterceptor;
import jocture.todo.web.interceptor.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // @Controller, @Service, @Repository, @Component, @Bean
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;
    private final AuthenticationCheckInterceptor authenticationCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(logInterceptor)
            .order(1)
            .addPathPatterns("/**");
        // "/*" 사용시 허용 : /a, /a?b
        //          미허용 : /a/b, /a/b/c
        // "/**" 사용시 허용 : /a, /a/b, /a/b/c

        //registry.addInterceptor(new LoginCheckInterceptor(userService))
        registry.addInterceptor(authenticationCheckInterceptor)
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/sessions", "/auth/**");
    }
}

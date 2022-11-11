package jocture.todo.config;

import jocture.todo.service.UserService;
import jocture.todo.web.auth.TokenProvider;
import jocture.todo.web.auth.UserAuthenticationHolder;
import jocture.todo.web.interceptor.AuthenticationCheckInterceptor;
import jocture.todo.web.interceptor.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // @Controller, @Service, @Repository, @Component, @Bean
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserAuthenticationHolder userAuthenticationHolder;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**");
        // "/*" 사용시 허용 : /a, /a?b
        //          미허용 : /a/b, /a/b/c
        // "/**" 사용시 허용 : /a, /a/b, /a/b/c

        //registry.addInterceptor(new LoginCheckInterceptor(userService))
        registry.addInterceptor(new AuthenticationCheckInterceptor(userService, tokenProvider, userAuthenticationHolder))
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/sessions", "/auth/**");
    }
}

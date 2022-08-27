package jocture.todo.config;

import jocture.todo.web.interceptor.LogInterceptor;
import jocture.todo.web.interceptor.LoginCheckInterceptor;
import jocture.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**");
        // "/*" 사용시 허용 : /a, /a?b
        //          미허용 : /a/b, /a/b/c
        // "/**" 사용시 허용 : /a, /a/b, /a/b/c

        registry.addInterceptor(new LoginCheckInterceptor(userService))
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/sessions", "/auth/**");
    }
}

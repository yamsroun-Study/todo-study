package jocture.todo.web.interceptor;

import jocture.todo.data.entity.User;
import jocture.todo.exception.InvalidUserException;
import jocture.todo.exception.RequiredAuthenticationException;
import jocture.todo.service.UserService;
import jocture.todo.web.auth.TokenProvider;
import jocture.todo.web.auth.UserAthenticationHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component //스프링 빈(bean)과 일반 객체 차이 스스로 정리해 볼 것!
//스프링 빈 생성 방법 : @Component, @Controller, @Service, @Respository, @Configuration, @Bean
//스프링 (DI, IoC) 컨테이너
// - DI : 의존성 주입(Dependency Injection()
// - IoC: 제어의 역전(Inversion of Control)
// - PSA: 포터블 서비스 추상화(Portable Service Abstraction)
@RequiredArgsConstructor
public class AuthenticationCheckInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserAthenticationHolder userAuthenticationHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 시작={}", requestURI);

        String authToken = parseAuthToken(request);
        String userId = validateAuthTokenAndGetUserId(authToken);
        User user = validateUserIdAndGetUser(userId);
        setUserToAuthenticationHolder(user);
        return true;
    }

    private String parseAuthToken(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        log.info("AuthToken: {}", authToken);
        if (!StringUtils.hasText(authToken) || !authToken.startsWith("Bearer ")) {
            return null;
        }
        return authToken.substring(7);
    }

    private String validateAuthTokenAndGetUserId(String authToken) {
        if (!StringUtils.hasText(authToken)) {
            throwNoLoginException();
        }
        return tokenProvider.validateAndGetUserId(authToken);
    }

    private User validateUserIdAndGetUser(String userId) {
        return userService.getUser(userId)
            .orElseThrow(() -> new InvalidUserException("유효한 회원이 아닙니다."));
    }

    private void setUserToAuthenticationHolder(User user) {
        userAuthenticationHolder.setUser(user);
    }

    private void throwNoLoginException() {
        throw new RequiredAuthenticationException("로그인을 하셔야 합니다.");
    }
}

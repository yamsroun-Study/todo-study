package jocture.todo.web.interceptor;

import jocture.todo.data.entity.User;
import jocture.todo.exception.InvalidUserException;
import jocture.todo.exception.RequiredAuthenticationException;
import jocture.todo.service.UserService;
import jocture.todo.web.auth.TokenProvider;
import jocture.todo.web.auth.UserAuthenticationHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationCheckInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserAuthenticationHolder userAuthenticationHolder;

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
        userAuthenticationHolder.set(user);
    }

    private void throwNoLoginException() {
        throw new RequiredAuthenticationException("로그인을 하셔야 합니다.");
    }
}

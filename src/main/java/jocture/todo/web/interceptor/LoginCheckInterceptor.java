package jocture.todo.web.interceptor;

import jocture.todo.web.controller.session.SessionConst;
import jocture.todo.data.entity.User;
import jocture.todo.exception.InvalidUserException;
import jocture.todo.exception.RequiredAuthenticationException;
import jocture.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.*;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 시작={}", requestURI);
        checkLoginSession(request.getSession(false));
        return true;
    }

    private void checkLoginSession(HttpSession session) {
        checkSessionAttribute(session);
        User loginUser = (User) session.getAttribute(SessionConst.SESSION_USER);
        String userId = loginUser.getId();
        checkLoginUser(userId);
    }

    private void checkSessionAttribute(HttpSession session) {
        if (session == null || session.getAttribute(SessionConst.SESSION_USER) == null) {
            throwNoLoginException();
        }
    }

    private void checkLoginUser(String userId) {
        if (!StringUtils.hasText(userId)) {
            throwNoLoginException();
        }
        if (!userService.existsUser(userId)) {
            throw new InvalidUserException("유효한 회원이 아닙니다.");
        }
    }

    private void throwNoLoginException() {
        throw new RequiredAuthenticationException("로그인을 하셔야 합니다.");
    }
}

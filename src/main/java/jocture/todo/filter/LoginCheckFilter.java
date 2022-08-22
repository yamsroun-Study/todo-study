package jocture.todo.filter;

import jocture.todo.controller.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] WHITE_LIST = {"/", "/sessions", "/auth/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request; // 다운 캐스팅
        String requestURI = httpRequest.getRequestURI();

        try {
            log.info("로그인 체크 필터 시작={}", requestURI);
            if (isLoginCheckTargetPath(requestURI)) {
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.SESSION_USER_KEY) == null) {
                    log.error("미로그인 사용자 요청={}", requestURI);
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(403);
                    return;
                }
            }
            chain.doFilter(request, response);
        } finally {
            log.info("로그인 체크 필터 종료={}", requestURI);
        }
    }

    private boolean isLoginCheckTargetPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}

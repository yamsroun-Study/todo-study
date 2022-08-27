package jocture.todo.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

//서블릿 컨테이너와 스프링 컨테이너
//스프링을 사용하는 경우에, 서블릿은 스프링의 DispatcherServlet 이다.

//요청 흐름
//: HTTP 요청 -> WAS(Tomcat) -> 필터 -> <스프링 시작> 서블릿(DispatcherServlet) -> 컨트롤러
//: HTTP 요청 -> WAS(Tomcat) -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(">>> init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        // 타입을 바꾸는 것을 Cast
        // 상위 타입에서 하위 타입으로 Cast : 다운 캐스팅
        // 하위 타입에서 상위 타입으로 Cast : 업 캐스팅
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        StringBuffer requestURL = httpRequest.getRequestURL();
        String requestURI = httpRequest.getRequestURI();
        log.info("requestURL={}", requestURL);
        log.info("requestURI={}", requestURI);

        String uuid = UUID.randomUUID().toString();

        log.info(">>> Request=[{}][{}]", uuid, requestURI);
        try {
            chain.doFilter(request, response);
        } finally {
            log.info(">>> Response=[{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info(">>> destroy()");
    }
}

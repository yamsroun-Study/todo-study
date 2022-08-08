package jocture.todo.controller.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {
        //세션 스토어에 세션ID-세션값을 저장
        //서버에서만 일어나는 일이므로 클라이언트가 알지 못함
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //클라이언트와 상태 유지를 하기 위해, 쿠키로 세션ID 전달 (세션값X)
        Cookie sessionCookie = new Cookie(SessionConst.SESSION_ID, sessionId);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
        // 쿠키 구분
        // - 영속 쿠키 : 만료 날짜가 있는 것 (만료날짜까지 쿠키 유지)
        // - 세션 쿠키 : 만료 날짜가 없는 것 (브라우저 종료 시까지 유지)
    }

    public Object getSession(HttpServletRequest request) {
        return getSessionCookie(request)
            // 세션ID 값으로 세션 스토어에서 세션ID에 저장된 세션값 조회
            .map(sessionStore::get)
            .orElse(null);
    }

    private Optional<String> getSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies())
            .orElseGet(() -> new Cookie[] {});
        return Arrays.stream(cookies)
            // 쿠키에서 세션ID 조회
            .filter(cookie -> cookie.getName().equals(SessionConst.SESSION_ID))
            .findFirst()
            // 세션ID 쿠키에서 세션ID 값 조회
            .map(Cookie::getValue);
    }

    public void expireSession(HttpServletRequest request) {
        Optional<String> cookie = getSessionCookie(request);
        //cookie.map(sessionStore::remove); // 중간 연산 메서드, 종단 연산(terminal opertaion) 메서드
        cookie.ifPresent(sessionStore::remove);
    }
}

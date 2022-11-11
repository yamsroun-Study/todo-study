package jocture.todo.web.auth;

import org.springframework.stereotype.Component;

//동시성 문제: 여러 요청에 대해 동시에 처리할 때 발생하는 문제
@Component
public class UserIdAuthenticationHolder implements AuthenticationHolder {

    private String userId;

    @Override
    public String get() {
        return userId;
    }

    @Override
    public void set(Object userId) {
        this.userId = (String) userId;
    }
}

package jocture.todo.study;

import jocture.todo.study.factory.LoginServiceFactory;
import jocture.todo.study.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginClient { // OCP(Open-Closed Principal) : 개방-폐쇄 원칙

    private final LoginServiceFactory loginServiceFactory;

    public void login(LoginType loginType) {
        LoginService loginService = loginServiceFactory.find(loginType);
        loginService.login();
    }


}

package jocture.todo.study.service;

import jocture.todo.study.LoginType;

public interface LoginService {

    boolean supports(LoginType loginType);

    void login();
}

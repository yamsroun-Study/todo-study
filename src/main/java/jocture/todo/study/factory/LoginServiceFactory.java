package jocture.todo.study.factory;

import jocture.todo.study.LoginType;
import jocture.todo.study.service.LoginService;

public interface LoginServiceFactory {

    LoginService find(LoginType loginType);
}

package jocture.todo.study.service;

import jocture.todo.study.LoginType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
    // 접근제한자(modifier) : public, protected, private, package-private(DEFAULT)
class WebLoginService implements LoginService {

    @Override
    public boolean supports(LoginType loginType) {
        return loginType == LoginType.WEB;
    }

    @Override
    public void login() {
        log.debug(">>> login()");
    }
}

package jocture.todo.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

@SpringBootTest
class LoginClientTest {

    @Autowired
    LoginClient loginClient;

    @ParameterizedTest
    @EnumSource
        //(mode = EnumSource.Mode.EXCLUDE, names = {"NAVER"})
    void find(LoginType loginType) {
        loginClient.login(loginType);
    }

    @Disabled
    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {"NAVER"})
    void notFound(LoginType loginType) {
        Assertions.assertThatThrownBy(() -> loginClient.login(loginType))
            .isInstanceOf(NoSuchElementException.class);
    }
}
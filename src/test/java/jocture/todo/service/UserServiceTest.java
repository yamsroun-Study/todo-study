package jocture.todo.service;

import jocture.todo.data.entity.User;
import jocture.todo.exception.ApplicationException;
import jocture.todo.exception.LoginFailException;
import jocture.todo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService service;

    @Mock // fake-double, Mock, Stub -> 가짜
    UserRepository repository;

    @Test
    void signUp_userIsNull() {
        // Given
        User user = null;
        // When & Then
        assertThatThrownBy(() -> service.signUp(user))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void signUp_emailIsNull() {
        // Given
        String email = null;
        String password = "PaSsWoRd";
        User user = createUser(null, password);
        // When & Then
        assertThatThrownBy(() -> service.signUp(user))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void signUp_emailAlreadyExists() {
        // Given
        String email = "test@abc.com";
        String password = "PaSsWoRd";
        User user = createUser(email, password);
        // Method Mocking
        doReturn(true)
            .when(repository)
            .existsByEmail(any());
        // When & Then
        assertThatThrownBy(() -> service.signUp(user))
            .isInstanceOf(ApplicationException.class);
    }

    @Test
    void signUp_success() {
        // Given
        String email = "test@abc.com";
        String password = "PaSsWoRd";
        User user = createUser(email, password);
        log.info(">>> repository : {}", repository);
        // When
        service.signUp(user);
        // Then
        verify(repository, times(1)).save(user);
    }

    private User createUser(String email, String password) {
        return User.builder()
            .username("TEST")
            .email(email)
            .password(password)
            .build();
    }

    // 숙제 (2022.05.21)
    // logIn() 메소드 테스트 코드 작성해오기 !!
    // 조건 : 커버리지 100%

    @Test
    void logIn() {
        // Given
        String email = "test@abc.com";
        String password = "PaSsWoRd";
        User user = createUser(email, password); // TDD(Test Driven Development) 방식
        // repository.findByEmailAndPassword(email, password); // Option 리턴
        doReturn(Optional.of(user))
            .when(repository)
            .findByEmailAndPassword(email, password);
        // When
        User result = service.logIn(email, password);
        // Then
        assertThat(result).isEqualTo(user) // equals() 비교
            .isSameAs(user); // == 비교
    }

    @Test
    void logIn_noResult() {
        // Given
        String email = "test@abc.com";
        String password = "PaSsWoRd";
        doReturn(Optional.empty())
            .when(repository)
            .findByEmailAndPassword(email, password);
        // When & Then
        assertThatThrownBy(() -> service.logIn(email, password))
            .isInstanceOf(LoginFailException.class);
    }

    @ParameterizedTest(name = "[{index}] {0} is blank value")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\n", "\r", "\r\n", "\t"})
    void logIn_noEmail(String email) {
        // Given
        String password = "PaSsWoRd";
        // When & Then
        assertThatThrownBy(() -> service.logIn(email, password))
            .isInstanceOf(ApplicationException.class);
    }

    @ParameterizedTest(name = "[{index}] {0} is blank value")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\n", "\r", "\r\n", "\t"})
    void logIn_noPassword(String password) {
        // Given
        String email = "test@abc.com";
        // When & Then
        assertThatThrownBy(() -> service.logIn(email, password))
            .isInstanceOf(ApplicationException.class);
    }

}
package jocture.todo.service;

import jocture.todo.data.entity.User;
import jocture.todo.exception.ApplicationException;
import jocture.todo.exception.LoginFailException;
import jocture.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service // @Component + Service 레이어 역할 (논리적) 표현 (기능 없음)
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional // CUD 가능
    public void signUp(User user) {
        if (user == null || user.getEmail() == null) {
            log.error("Invalid user: {}", user);
            throw new ApplicationException("Invalid user");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("Email already exists: {}", user.getEmail());
            throw new ApplicationException("Email already exists");
        }
        userRepository.save(user);
    }

    public User logIn(String email, String password) {
        if (!StringUtils.hasText(email)) {
            log.warn("Email is blank");
            throw new ApplicationException("Email is blank");
        }
        if (!StringUtils.hasText(password)) {
            log.warn("Password is blank");
            throw new ApplicationException("Password is blank");
        }
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        return user.orElseThrow(() -> new LoginFailException("아이디 또는 패스워드가 잘못되었습니다."));
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }

    public boolean existsUser(String userId) {
        return getUser(userId).isPresent();
    }
}

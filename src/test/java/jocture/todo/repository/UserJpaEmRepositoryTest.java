package jocture.todo.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import jocture.todo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class UserJpaEmRepositoryTest {

    @Autowired
    UserJpaEmRepository repository;

    @Test
    void basic() {
        // Given
        String email = "test@abc.com";
        String password = "";
        User user = createUser(email, password);
        // When
        repository.save(user);
        Boolean exists = repository.existsByEmail(email);
        Optional<User> result = repository.findByEmail(email);
        // Then
        assertThat(exists).isTrue();
        assertThat(result).isPresent()
            .contains(user)      // equals() 비교
            .containsSame(user); // ==(참조) 비교
        // assertThat(result.get()).isEqualTo(user);
        // assertThat(result.get()).isSameAs(user);
    }

    @Test
    void basic_noResult() {
        // Given
        String email = "test@abc.com";
        String password = "";
        User user = createUser(email, password);
        // When
        Boolean exists = repository.existsByEmail(email);
        Optional<User> result = repository.findByEmail(email);
        // Then
        assertThat(exists).isFalse();
        assertThat(result).isEmpty();
    }

    private User createUser(String email, String password) {
        return User.builder()
            .username("TEST")
            .email(email)
            .password(password)
            .build();
    }

    @Test
    void findByEmailAndPassword() {
        // Given
        String email = "test@abc.com";
        String password = "PaSsWoRd";
        User user = createUser(email, password);
        repository.save(user);
        // When
        Optional<User> result = repository.findByEmailAndPassword(email, password);
        // Then
        assertThat(result).isPresent()
            .contains(user)
            .containsSame(user);
    }

    @Test
    void findByEmailAndPassword_noResult() {
        // Given
        String email = "test@abc.com";
        String password = "PaSsWoRd";
        User user = createUser(email, password);
        // When
        Optional<User> result = repository.findByEmailAndPassword(email, password);
        // Then
        assertThat(result).isEmpty();
    }
}

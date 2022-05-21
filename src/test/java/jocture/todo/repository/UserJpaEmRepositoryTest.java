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
        User user = createUser(email);

        // When
        repository.save(user);
        Boolean exists = repository.existsByEmail(email);
        Optional<User> result = repository.findByEmail(email);

        // Then
        assertThat(result).isPresent()
            .contains(user)      // equals() 비교
            .containsSame(user); // ==(참조) 비교
        // assertThat(result.get()).isEqualTo(user);
        // assertThat(result.get()).isSameAs(user);
        assertThat(exists).isTrue();
    }

    private User createUser(String email) {
        return User.builder()
            .username("TEST")
            .email(email)
            .password("PaSsWoRd")
            .build();
    }

    @Test
    void findByEmailAndPassword() {
        // Todo
    }
}

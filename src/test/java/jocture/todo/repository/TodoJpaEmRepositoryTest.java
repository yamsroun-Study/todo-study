package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@SpringBootTest
@Transactional
class TodoJpaEmRepositoryTest {

    private static final String USER_NAME = "temp";

    @Autowired EntityManager em;
    @Autowired TodoRepository repository;

    @Test
    void findAll() {
        // 숙제
    }

    @Test
    void findById() {
        // 숙제
    }

    @Test
    void findByUserId() {
        // 숙제
    }

    @Test
    // @Rollback(false)
    void save() {
        // Given
        Todo todo = Todo.builder() // 빌더 패턴
            .userId(USER_NAME)
            .title("자바 공부하기!")
            .done(false)
            .build(); // build/out 디렉토리에 자동 생성되는 코드 구조 확인하기!
        // When
        repository.save(todo);
        // Then
        // 숙제
    }

    @Disabled
    @Test
    void delete() {
    }

    @Disabled
    @Test
    void deleteById() {
    }
}
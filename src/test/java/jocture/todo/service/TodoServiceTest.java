package jocture.todo.service;

import jocture.todo.entity.Todo;
import jocture.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional  // @SpringBootTest의 @Transactional의 기본 설정은 Rollback
                // Main 코드의 @Transactional과 무관하게 Rollback을 위해 꼭 넣어주자!
class TodoServiceTest {

    private static final String USER_NAME = "temp";

    @Autowired TodoService service;
    @Autowired TodoRepository repository;

    @Disabled
    @Test
    void getList() {

    }

    @Test
    void create() {
        // Given
        Todo todo = Todo.builder()
            .userId(USER_NAME)
            .title("자바 공부하기")
            .build();
        // When
        service.create(todo);
        // Then
        Optional<Todo> result = repository.findById(todo.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isPositive();
        assertThat(result.get().getTitle()).isEqualTo(todo.getTitle());
    }

    @Disabled
    @Test
    void create_invalidEntity() {

    }

    @Disabled
    @Test
    void update() {

    }

    @Disabled
    @Test
    void update_invalidEntity() {

    }

    @Disabled
    @Test
    void delete() {

    }

    @Disabled
    @Test
    void delete_invalidEntity() {

    }
}
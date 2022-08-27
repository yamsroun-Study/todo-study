package jocture.todo.repository;

import jocture.todo.data.entity.Todo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class TodoJpaEmRepositoryTest {

    private static final String USER_NAME = "temp";

    @Autowired EntityManager em;
    @Autowired TodoRepository repository;

    @Test
    void findAll() {
        // Given
        saveTodo("자바 공부하기");
        saveTodo("스프링 공부하기");
        // When
        List<Todo> result = repository.findAll();
        // Then
        assertThat(result).hasSize(2);
    }

    private Todo saveTodo(String title) {
        // Given
        Todo todo = Todo.builder() // 빌더 패턴
            .userId(USER_NAME)
            .title(title)
            .build(); // build/out 디렉토리에 자동 생성되는 코드 구조 확인하기!
        repository.save(todo);
        return todo;
    }

    @Test
    void findById() {
        // Given
        Todo todo1 = saveTodo("자바 공부하기");
        Todo todo2 = saveTodo("스프링 공부하기");
        // When
        Optional<Todo> result = repository.findById(todo2.getId());
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(todo2.getId());
    }

    @Test
    void findById_withoutResult() {
        // Given
        Integer id = -999;
        // When
        Optional<Todo> result = repository.findById(id);
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByUserId() {
        // Given
        Todo todo1 = saveTodo("자바 공부하기");
        Todo todo2 = saveTodo("스프링 공부하기");
        // When
        List<Todo> result = repository.findByUserId(todo1.getUserId());
        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void save() {
        // Given
        Todo todo = saveTodo("자바 공부하기");
        // When
        repository.save(todo);
        // Then
        Optional<Todo> result = repository.findById(todo.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(todo.getId());
    }

    @Test
    void delete() {
        // Given
        Todo todo = saveTodo("자바 공부하기");
        // When
        repository.delete(todo);
        // Then
        Optional<Todo> result = repository.findById(todo.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void deleteById() {
        // Given
        Todo todo = saveTodo("자바 공부하기");
        // When
        repository.deleteById(todo.getId());
        // Then
        Optional<Todo> result = repository.findById(todo.getId());
        assertThat(result).isEmpty();
    }
}

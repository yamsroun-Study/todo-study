package jocture.todo.service;

import jocture.todo.entity.Todo;
import jocture.todo.exception.ApplicationException;
import jocture.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@Transactional  // @SpringBootTest의 @Transactional의 기본 설정은 Rollback
    // Main 코드의 @Transactional과 무관하게 Rollback을 위해 꼭 넣어주자!
class TodoServiceTest {

    private static final String USER_NAME = "temp";

    @Autowired TodoService service;
    @Autowired TodoRepository repository;
    @Autowired EntityManager em;

    @Test
    void getList() {
        // Given
        int createCount = 3;
        createTodoList(createCount);
        // When
        List<Todo> result = service.getList(USER_NAME);
        // Then
        assertThat(result).hasSize(3);
    }

    private void createTodoList(int createCount) {
        for (int i = 0; i < createCount; i++) {
            Todo todo = Todo.builder()
                .userId(USER_NAME)
                .title("자바 공부하기 " + (i + 1))
                .build();
            service.create(todo);
        }
    }

    @Test
    void create() {
        // Given
        Todo todo = Todo.builder()
            .userId(USER_NAME)
            .title("자바 공부하기")
            .build();
        // When
        service.create(todo); // flush가 일어나지 않았기 때문에, INSERT 문이 DB에 아직 미전달
        // Then
        Optional<Todo> result = repository.findById(todo.getId()); // DB 조회를 위해, 아직 전달되지 않은 INSERT 문이 미리 전달됨 (flush 일어남)
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isPositive();
        assertThat(result.get().getTitle()).isEqualTo(todo.getTitle());
    }

    @Test
    @DisplayName("create() - Todo가 null인 경우")
    void create_invalidEntity() {
        // Given
        Todo todo = null;
        // When
        ThrowableAssert.ThrowingCallable callable = () -> service.create(todo);
        Executable executable = () -> service.create(todo);
        // Then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
        Assertions.assertThrows(ApplicationException.class, executable);
    }

    @Test
    @DisplayName("create() - Todo에 UserId가 null인 경우")
    void create_withoutUserId() {
        // Given
        Todo todo = Todo.builder()
            .title("자바 공부하기")
            .build();
        // When
        ThrowableAssert.ThrowingCallable callable = () -> service.create(todo);
        Executable executable = () -> service.create(todo);
        // Then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
        Assertions.assertThrows(ApplicationException.class, executable);
    }

    @Test
    void update() {
        // Given
        int createCount = 3;
        createTodoList(createCount);
        List<Todo> todos = service.getList(USER_NAME);
        Todo newTodo = todos.get(1);
        newTodo.setTitle("자바 공부 절대 안하기 !!!");
        newTodo.setDone(true);
        // When
        service.update(newTodo);
        // Then
        Optional<Todo> result = repository.findById(newTodo.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(newTodo.getTitle());
        assertThat(result.get().isDone()).isEqualTo(newTodo.isDone());
    }

    @Test
    @DisplayName("update() - Todo가 없는 경우")
    void update_emptyEntity() {
        // Given
        Todo newTodo = Todo.builder()
            .userId(USER_NAME)
            .title("자바 공부하기")
            .done(false)
            .build();
        newTodo.setId(-999);
        newTodo.setTitle("자바 공부 절대 안하기 !!!");
        newTodo.setDone(true);
        // When
        service.update(newTodo);
        // Then
    }

    @Test
    @DisplayName("update() - Todo가 null인 경우")
    void update_invalidEntity() {
        // Given
        Todo todo = null;
        // When
        ThrowableAssert.ThrowingCallable callable = () -> service.update(todo);
        // Then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("update() - Todo에 UserId가 null인 경우")
    void update_withoutUserId() {
        // Given
        Todo todo = Todo.builder()
            .title("자바 공부하기")
            .build();
        // When
        ThrowableAssert.ThrowingCallable callable = () -> service.update(todo);
        // Then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
    }

    @Test
    void delete() {
        // Given
        int createCount = 3;
        createTodoList(createCount);
        List<Todo> todos = service.getList(USER_NAME);
        Todo deletingTodo = todos.get(1); // 영속 상태
        // When
        service.delete(deletingTodo);
        // Then
        Optional<Todo> result = repository.findById(deletingTodo.getId());
        assertThat(result).isEmpty();
        System.out.println(">>> deletingTodo = " + deletingTodo); // 객체는 그대로 남아있음 -> 비영속 상태
        deletingTodo.setTitle("자바 공부 절대 안하기 !!"); // 비영속 상태이기 때문에 UPDATE 문이 발생하지 않는다!
        em.flush(); // 영속성 컨텍스트 강제 flush 요청
    }

}

package jocture.todo.controller;

import jocture.todo.entity.Todo;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 스프링 3계층(레이어) -> @Controller, @Service, @Repository

@RestController // == @Controller + @ResponseBody
@RequiredArgsConstructor
public class TodoController {

    private static final String TEMP_USER_ID = "temp";

    private final TodoService service;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제), OPTION, HEAD, TRACE
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 요청 바디 + 응답 바디)

    @GetMapping("/todo")
    public ResponseEntity<List<Todo>> getTodoList() {
        List<Todo> todos = service.getList(TEMP_USER_ID);
        return ResponseEntity.ok().body(todos);
    }

    @PostMapping("/todo")
    public ResponseEntity<List<Todo>> createTodo(
        String title
    ) {
        Todo todo = Todo.builder()
            .userId(TEMP_USER_ID)
            .title(title)
            .build();
        service.create(todo);
        return getTodoList();
    }

    @PutMapping("/todo")
    public ResponseEntity<List<Todo>> updateTodo(
        Integer id,
        String title,
        boolean done
    ) {
        Todo todo = Todo.builder()
            .id(id)
            .userId(TEMP_USER_ID)
            .title(title)
            .done(done)
            .build();

        service.update(todo);
        return getTodoList();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<List<Todo>> deleteTodo(
        Integer id
    ) {
        Todo todo = Todo.builder()
            .id(id)
            .userId(TEMP_USER_ID)
            .build();
        service.delete(todo);
        return getTodoList();
    }

}

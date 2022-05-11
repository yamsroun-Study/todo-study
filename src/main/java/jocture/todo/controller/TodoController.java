package jocture.todo.controller;

import jocture.todo.dto.TodoDto;
import jocture.todo.entity.Todo;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 스프링 3계층(레이어) -> @Controller, @Service, @Repository

@Slf4j
@RestController // == @Controller + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private static final String TEMP_USER_ID = "temp";

    private final TodoService service;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 요청 바디 + 응답 바디)

    // @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, methods = {RequestMethod.POST})
    @GetMapping
    public ResponseEntity<List<TodoDto>> getTodoList() {
        // 스프링 3대 요소 : IoC(DI), PSA, AOP

        log.debug("getTodoList()");
        List<Todo> todos = service.getList(TEMP_USER_ID);
        log.debug("getTodoList() 22");
        // JSON -> 객체를 표현하는 스트링
        // 객체를 JSON 스트링으로 변환 -> HttpMessageConverter (Serialize/Serializer)
        // JSON 스트링을 객체로 변환 -> HttpMessageConverter (Deserialize/Deserializer)

        // List<TodoDto> todoDtos = new ArrayList<>();
        // for (Todo todo: todos) {
            // TodoDto todoDto = TodoDto.builder()
            //     .id(todo.getId())
            //     .title(todo.getTitle())
            //     .done(todo.isDone())
            //     .build();
            // TodoDto todoDto = TodoDto.toDto(todo);
            // todoDtos.add(todoDto);
        // }
        return ResponseEntity.ok().body(TodoDto.toDtoList(todos));
    }

    // R&R -> Role & Responsibility
    @PostMapping
    public ResponseEntity<List<TodoDto>> createTodo(
        @RequestBody TodoDto todoDto // 기본 생성자로 객체 생성 -> Setter로 필드 할당(Reflection)
    ) {
        log.info(">>> todoDto: {}", todoDto);
        Todo todo = Todo.from(todoDto); // TodoDto를 Todo 객체로 변환한다.
        service.create(todo);
        return getTodoList();
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createTodo22222(
        @RequestBody String body
    ) {
        log.info(">>> body: {}", body);
        return "{\"title\":\"테스트\"}";
    }

    @PutMapping
    public ResponseEntity<List<TodoDto>> updateTodo(
        @RequestBody TodoDto todoDto
    ) {
        log.info(">>> todoDto: {}", todoDto);
        Todo todo = Todo.from(todoDto);
        service.update(todo);
        return getTodoList();
    }

    @DeleteMapping
    public ResponseEntity<List<TodoDto>> deleteTodo(
        @RequestBody TodoDto todoDto
    ) {
        log.info(">>> todoDto: {}", todoDto);
        Todo todo = Todo.from(todoDto);
        service.delete(todo);
        return getTodoList();
    }

}

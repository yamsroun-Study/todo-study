package jocture.todo.controller;

import jocture.todo.dto.TodoDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.Todo;
import jocture.todo.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 스프링 3계층(레이어) -> @Controller, @Service, @Repository

@Slf4j
@RestController // == @Controller + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/todo")
// @CrossOrigin(origins = {"http://localhost:3000"}, maxAge = 3600, methods = {GET, POST, PUT, DELETE})
public class TodoController {

    private static final String TEMP_USER_ID = "temp";

    private final TodoService service;
    private final TodoMapper todoMapper;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 요청 바디 + 응답 바디)

    // @CrossOrigin("*")
    @GetMapping
    public ResponseDto<List<TodoDto>> getTodoList() {
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

        // return ResponseEntity.ok().body(TodoDto.toDtoList(todos));
        // List<TodoDto> todos = new ArrayList<>();
        // for (Todo todo: todos) {
        //     todos.add(todoMapper.toDto(todo));
        // }

        ResponseResultDto<List<TodoDto>> responseData = ResponseResultDto.of(todoMapper.toDtoList(todos));
        return ResponseDto.of(responseData);
    }

    // R&R -> Role & Responsibility
    @PostMapping
    public ResponseDto<List<TodoDto>> createTodo(
        @RequestBody TodoDto todoDto
        // MappingJackson2HttpMessageConverter : Deserialize : 객체생성(디폴트생성자) -> getter/setter 메서드를 이용해 프로퍼티 찾아서 Reflection을 이용해 할당
    ) {
        log.info(">>> todoDto: {}", todoDto);
        // Todo todo = Todo.from(todoDto); // TodoDto를 Todo 객체로 변환한다.
        Todo todo = todoMapper.toEntity(todoDto);
        // todo.setUserId(TEMP_USER_ID); // 임시

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
    public ResponseDto<List<TodoDto>> updateTodo(
        @RequestBody TodoDto todoDto
    ) {
        log.info(">>> todoDto: {}", todoDto);
        // Todo todo = Todo.from(todoDto);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(TEMP_USER_ID); // 임시
        service.update(todo);
        return getTodoList();
    }

    @DeleteMapping
    public ResponseDto<List<TodoDto>> deleteTodo(
        @RequestBody TodoDto todoDto
    ) {
        log.info(">>> todoDto: {}", todoDto);
        // Todo todo = Todo.from(todoDto);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(TEMP_USER_ID); // 임시
        service.delete(todo);
        return getTodoList();
    }

}

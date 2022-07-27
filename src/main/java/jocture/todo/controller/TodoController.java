package jocture.todo.controller;

import jocture.todo.controller.validation.marker.TodoValidationGroup;
import jocture.todo.dto.TodoDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.Todo;
import jocture.todo.exception.NoAuthenticationException;
import jocture.todo.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

// 스프링 3계층(레이어) -> @Controller, @Service, @Repository

@Slf4j
@RestController // == @Controller + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/todo")
// @CrossOrigin(origins = {"http://localhost:3000"}, maxAge = 3600, methods = {GET, POST, PUT, DELETE})
public class TodoController {

    private final TodoService service;
    private final TodoMapper todoMapper;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 요청 바디 + 응답 바디)

    @GetMapping("/v1") //사용 금지
    public ResponseDto<List<TodoDto>> getTodoList(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userId = Arrays.stream(Optional.ofNullable(cookies).orElse(new Cookie[] {}))
            .filter(cookie -> cookie.getName().equals("userId"))
            .findFirst()
            //.map(cookie -> cookie.getValue())
            .map(Cookie::getValue)
            .orElse(null);
        log.debug(">>> userId : {}", userId);
        return getRealTodoList(userId);
    }

    @GetMapping("/v2")
    public ResponseDto<List<TodoDto>> getTodoList(
        @CookieValue(name = "userId", required = false) String userId
    ) {
        log.debug(">>> userId : {}", userId);
        validateUser(userId);
        return getRealTodoList(userId);
    }

    private void validateUser(String userId) {
        if (userId == null) {
            throw new NoAuthenticationException("로그인을 하셔야 합니다.");
        }
    }


    // @CrossOrigin("*")
    private ResponseDto<List<TodoDto>> getRealTodoList(String userId) {
        // 스프링 3대 요소 : IoC(DI), PSA, AOP

        log.debug("getTodoList()");
        List<Todo> todos = service.getList(userId);
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
    @PostMapping()
    public ResponseDto<List<TodoDto>> createTodo(
        // @PathVariable String id, // URL의 Path 변수에서 가져옴
        // @RequestParam String title, // Query String 또는 Body 데이터(Content-Type이 x-www-form-urlencoded인 경우)
        // @ModelAttribute TodoDto todoDto,
        @RequestBody @Validated({TodoValidationGroup.Creation.class}) TodoDto todoDto,
        // MappingJackson2HttpMessageConverter : Deserialize : 객체생성(디폴트생성자) -> getter/setter 메서드를 이용해 프로퍼티 찾아서 Reflection을 이용해 할당
        @CookieValue(name = "userId", required = false) String userId
    ) {
        validateUser(userId);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(userId);

        service.create(todo);
        return getRealTodoList(userId);
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
        @RequestBody @Validated({TodoValidationGroup.Update.class}) TodoDto todoDto,
        @CookieValue(name = "userId", required = false) String userId
    ) {
        validateUser(userId);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(userId);
        service.update(todo);
        return getRealTodoList(userId);
    }

    @DeleteMapping
    public ResponseDto<List<TodoDto>> deleteTodo(
        @RequestBody @Validated({TodoValidationGroup.Deletion.class}) TodoDto todoDto,
        @CookieValue(name = "userId", required = false) String userId
    ) {
        validateUser(userId);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(userId);
        service.delete(todo);
        return getRealTodoList(userId);
    }

}

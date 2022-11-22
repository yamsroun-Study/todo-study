package jocture.todo.web.controller;

import jocture.todo.data.dto.TodoDto;
import jocture.todo.data.dto.response.ResponseDto;
import jocture.todo.data.dto.response.ResponseResultDto;
import jocture.todo.data.entity.Todo;
import jocture.todo.data.entity.User;
import jocture.todo.data.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import jocture.todo.web.argument.LoginUser;
import jocture.todo.web.auth.UserAthenticationHolder;
import jocture.todo.web.controller.validation.marker.TodoValidationGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todo/v7")
public class TodoV7Controller {

    private final UserAthenticationHolder userAuthenticationHolder;
    private final TodoService todoService;
    private final TodoMapper todoMapper;

    //횡단(공통) 관심사(Cross-cutting concern)

    @GetMapping
    public ResponseDto<List<TodoDto>> getTodoList(
        //@LoginUser User loginUser
    ) {
        User loginUser = userAuthenticationHolder.getUser();
        log.debug(">>> loginUser  : {}", loginUser);
        return getRealTodoList(loginUser.getId());
    }

    private ResponseDto<List<TodoDto>> getRealTodoList(String userId) {
        // 스프링 3대 요소 : IoC(DI), PSA, AOP

        List<Todo> todos = todoService.getList(userId);
        // JSON -> 객체를 표현하는 스트링
        // 객체를 JSON 스트링으로 변환 -> HttpMessageConverter (Serialize/Serializer)
        // JSON 스트링을 객체로 변환 -> HttpMessageConverter (Deserialize/Deserializer)

        ResponseResultDto<List<TodoDto>> responseData = ResponseResultDto.of(todoMapper.toDtoList(todos));
        return ResponseDto.of(responseData);
    }

    @PostMapping
    public ResponseDto<List<TodoDto>> createTodo(
        @RequestBody @Validated({TodoValidationGroup.Creation.class}) TodoDto todoDto,
        @LoginUser User loginUser
    ) {
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(loginUser.getId());

        todoService.create(todo);
        return getRealTodoList(loginUser.getId());
    }

    @PutMapping
    public ResponseDto<List<TodoDto>> updateTodo(
        @RequestBody @Validated({TodoValidationGroup.Update.class}) TodoDto todoDto,
        @LoginUser User loginUser
    ) {
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(loginUser.getId());
        todoService.update(todo);
        return getRealTodoList(loginUser.getId());
    }

    @DeleteMapping
    public ResponseDto<List<TodoDto>> deleteTodo(
        @RequestBody @Validated({TodoValidationGroup.Deletion.class}) TodoDto todoDto,
        @LoginUser User loginUser
    ) {
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(loginUser.getId());
        todoService.delete(todo);
        return getRealTodoList(loginUser.getId());
    }

}

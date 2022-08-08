package jocture.todo.controller;

import jocture.todo.controller.session.SessionConst;
import jocture.todo.controller.session.SessionManager;
import jocture.todo.controller.validation.marker.TodoValidationGroup;
import jocture.todo.dto.TodoDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.Todo;
import jocture.todo.entity.User;
import jocture.todo.exception.InvalidUserException;
import jocture.todo.exception.RequiredAuthenticationException;
import jocture.todo.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import jocture.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;

// 스프링 3계층(레이어) -> @Controller, @Service, @Repository

@Slf4j
@RestController // == @Controller + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/todo")
// @CrossOrigin(origins = {"http://localhost:3000"}, maxAge = 3600, methods = {GET, POST, PUT, DELETE})
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;
    private final TodoMapper todoMapper;
    private final SessionManager sessionManager;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 요청 바디 + 응답 바디)

    @Deprecated(since = "2.0", forRemoval = true)
    @GetMapping("/v1") //사용 금지
    public ResponseDto<List<TodoDto>> getTodoListV1(HttpServletRequest request) {
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
    public ResponseDto<List<TodoDto>> getTodoListV2(
        @CookieValue(name = "userId", required = false) String userId
    ) {
        log.debug(">>> userId : {}", userId);
        validateUser(userId);
        return getRealTodoList(userId);
    }

    @GetMapping("/v3")
    public ResponseDto<List<TodoDto>> getTodoListV3(
        HttpServletRequest request
    ) {
        User loginUser = (User) sessionManager.getSession(request);
        log.debug(">>> loginUser : {}", loginUser);
        validateUser(loginUser);
        return getRealTodoList(loginUser.getId());
    }

    @GetMapping("/v4")
    public ResponseDto<List<TodoDto>> getTodoListV4(
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute(SessionConst.SESSION_USER_KEY);

        log.debug(">>> loginUser  : {}", loginUser);
        validateUser(loginUser);
        return getRealTodoList(loginUser.getId());
    }

    @GetMapping("/v5")
    public ResponseDto<List<TodoDto>> getTodoListV5(
        HttpSession session
    ) {
        User loginUser = (User) session.getAttribute(SessionConst.SESSION_USER_KEY);

        log.debug(">>> loginUser  : {}", loginUser);
        validateUser(loginUser);
        return getRealTodoList(loginUser.getId());
    }

    @GetMapping("/v6")
    public ResponseDto<List<TodoDto>> getTodoListV6(
        @SessionAttribute(name = SessionConst.SESSION_USER_KEY, required = false) User loginUser
    ) {
        log.debug(">>> loginUser  : {}", loginUser);
        validateUser(loginUser);
        return getRealTodoList(loginUser.getId());
    }

    private void validateUser(User user) {
        if (user == null) {
            throwNoLoginException();
        }
        validateUser(user.getId());
    }

    private static void throwNoLoginException() {
        throw new RequiredAuthenticationException("로그인을 하셔야 합니다.");
    }

    private void validateUser(String userId) {
        if (!StringUtils.hasText(userId)) {
            throwNoLoginException();
        }
        if (!userService.existsUser(userId)) {
            throw new InvalidUserException("유효한 회원이 아닙니다.");
        }
    }

    // @CrossOrigin("*")
    private ResponseDto<List<TodoDto>> getRealTodoList(String userId) {
        // 스프링 3대 요소 : IoC(DI), PSA, AOP

        List<Todo> todos = todoService.getList(userId);
        // JSON -> 객체를 표현하는 스트링
        // 객체를 JSON 스트링으로 변환 -> HttpMessageConverter (Serialize/Serializer)
        // JSON 스트링을 객체로 변환 -> HttpMessageConverter (Deserialize/Deserializer)

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

        todoService.create(todo);
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
        todoService.update(todo);
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
        todoService.delete(todo);
        return getRealTodoList(userId);
    }

}

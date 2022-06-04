package jocture.todo.controller;

import jocture.todo.dto.UserDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.User;
import jocture.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController // @Controller + @ResponseBody
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseDto<UserDto> signUp(
        @RequestBody UserDto userDto
        // MappingJackson2HttpMessageConverter : Deserialize : 객체생성(디폴트생성자) -> getter/setter 메서드를 이용해 프로퍼티 찾아서 Reflection을 이용해 할당
    ) {
        log.debug(">>> userDto : {}", userDto);

        // UserDto -> User 변환
        User user = User.builder()
            .username(userDto.getUsername())
            .email(userDto.getEmail())
            .password(userDto.getPassword())
            .build();

        try {
            // Service Layer(서비스 계층)에 위임
            userService.signUp(user);

            // 타입 추론 : 컴파일러가 빌드 타입에 타입을 추론할 수 있어야 한다.
            // -> 대표적인게 람다식
            // -> Java 10에서 var 키워드 추가됨 (Kotlin 언어에서 쓰는 방식)

            // 응답
            var responseUserDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

            // ResponseResultPageDto dataPage = ResponseResultPageDto.of(1, 20, 15);
            ResponseResultDto<UserDto> resultDto = ResponseResultDto.of(responseUserDto/*, dataPage*/);
            return ResponseDto.of(resultDto);
        } catch (Exception e) {
            log.error("signUp() Exception ->", e);
            throw e;
        }
    }

    @ExceptionHandler
    public ResponseEntity<?> exceptionHandler(Exception e) {
        log.error("exceptionHandler ->", e);
        return ResponseEntity.badRequest().body("ERROR");
    }

    @PostMapping("/login")
    public ResponseDto<UserDto> logIn(
        @RequestBody UserDto userDto
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        User user = userService.logIn(email, password);

        UserDto responseUserDto = UserDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .build();

        // Parameter : 20 -> Response 15

        ResponseResultDto<UserDto> resultDto = ResponseResultDto.of(responseUserDto);
        return ResponseDto.of(resultDto);
    }
}

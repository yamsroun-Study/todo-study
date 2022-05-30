package jocture.todo.controller;

import jocture.todo.dto.UserDto;
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
    public UserDto signUp(
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

            // 응답
            UserDto responseUserDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
            return responseUserDto; // 디폴트 : 200 OK

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
}

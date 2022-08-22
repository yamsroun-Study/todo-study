package jocture.todo.controller;

import jocture.todo.controller.session.SessionConst;
import jocture.todo.controller.session.SessionManager;
import jocture.todo.controller.validation.marker.UserValidationGroup;
import jocture.todo.dto.UserDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.User;
import jocture.todo.mapper.UserMapper;
import jocture.todo.service.UserService;
import jocture.todo.type.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;

@Slf4j
@RestController // @Controller + @ResponseBody
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final SessionManager sessionManager;

    @PostMapping("/signup")
    public ResponseDto<UserDto> signUp(
        @RequestBody @Validated({UserValidationGroup.SignUp.class}) UserDto userDto
        // MappingJackson2HttpMessageConverter : Deserialize : 객체생성(디폴트생성자) -> getter/setter 메서드를 이용해 프로퍼티 찾아서 Reflection을 이용해 할당
    ) {
        log.debug(">>> userDto : {}", userDto);

        // UserMapperImpl userMapper = new UserMapperImpl();

        // UserDto -> User 변환
        // User user = User.builder()
        //     .username(userDto.getUsername())
        //     .email(userDto.getEmail())
        //     .password(userDto.getPassword())
        //     .build();
        // log.debug(">>> user : {}", user);

        User user = userMapper.toEntity(userDto);
        // log.debug(">>> user : {}", user);

        // Service Layer(서비스 계층)에 위임
        userService.signUp(user);

        // 타입 추론 : 컴파일러가 빌드 타입에 타입을 추론할 수 있어야 한다.
        // -> 대표적인게 람다식
        // -> Java 10에서 var 키워드 추가됨 (Kotlin 언어에서 쓰는 방식)

        // 응답
        // UserDto responseUserDto = userMapper.toDto(user);
        // var responseUserDto = UserDto.builder()
        //     .id(user.getId())
        //     .username(user.getUsername())
        //     .email(user.getEmail())
        //     .build();

        // ResponseResultPageDto dataPage = ResponseResultPageDto.of(1, 20, 15);
        ResponseResultDto<UserDto> resultDto = ResponseResultDto.of(userMapper.toDto(user)/*, dataPage*/);
        // return ResponseEntity.ok(ResponseDto.of(resultDto));
        return ResponseDto.of(resultDto);
    }

    @PostMapping("/login/v1")
    public ResponseDto<UserDto> logInV1(
        @RequestBody @Validated({UserValidationGroup.Login.class}) UserDto userDto,
        HttpServletResponse response
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userService.logIn(email, password);

        Cookie idCookie = new Cookie("userId", user.getId());
        idCookie.setPath("/");
        response.addCookie(idCookie);

        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/login/v2")
    public ResponseDto<UserDto> logInV2(
        @RequestBody @Validated({UserValidationGroup.Login.class}) UserDto userDto,
        HttpServletResponse response
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userService.logIn(email, password);

        sessionManager.createSession(user, response);

        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/login/v3")
    public ResponseDto<UserDto> logInV3(
        @RequestBody @Validated({UserValidationGroup.Login.class}) UserDto userDto,
        HttpServletRequest request
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userService.logIn(email, password);

        HttpSession session = request.getSession(); //세션 생성
        session.setAttribute(SessionConst.SESSION_USER_KEY, user); //세션에 데이터 저장
        //session.setMaxInactiveInterval(5); // 만료시간: 5초

        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/logout/v1")
    public ResponseDto<UserDto> logOutV1(
        HttpServletResponse response
    ) {
        Cookie idCookie = new Cookie("userId", "");
        idCookie.setPath("/");
        idCookie.setMaxAge(0);
        response.addCookie(idCookie);
        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/logout/v2")
    public ResponseDto<UserDto> logOutV2(
        HttpServletRequest request
    ) {
        sessionManager.expireSession(request);
        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/logout/v3")
    public ResponseDto<UserDto> logOutV3(
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseDto.of(ResponseCode.SUCCESS);
    }
}

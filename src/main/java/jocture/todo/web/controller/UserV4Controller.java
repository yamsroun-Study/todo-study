package jocture.todo.web.controller;

import jocture.todo.data.dto.UserDto;
import jocture.todo.data.dto.response.ResponseDto;
import jocture.todo.data.dto.response.ResponseResultDto;
import jocture.todo.data.entity.User;
import jocture.todo.service.UserService;
import jocture.todo.web.auth.TokenProvider;
import jocture.todo.web.controller.validation.marker.UserValidationGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/auth/v4")
@RequiredArgsConstructor
public class UserV4Controller {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseDto<UserDto> logIn(
        @RequestBody @Validated({UserValidationGroup.Login.class}) UserDto userDto,
        HttpServletRequest request
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userService.logIn(email, password);
        String authToken = tokenProvider.create(user);
        UserDto responseUserDto = UserDto.builder().token(authToken).build();
        return ResponseDto.of(ResponseResultDto.of(responseUserDto));
    }
}

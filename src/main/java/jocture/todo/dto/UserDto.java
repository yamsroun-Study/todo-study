package jocture.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jocture.todo.controller.validation.marker.UserValidationGroup;
import lombok.*;

import javax.validation.constraints.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 당장은 다른 생성자가 없을 때도, 정의해 둬야 나중에 오류 발생 가능성이 줄어든다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter //@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @NotBlank(groups = {UserValidationGroup.SignUp.class})
    private String username;

    @Email(groups = {UserValidationGroup.SignUp.class, UserValidationGroup.Login.class})
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "올바른 패스워드 형식이 아닙니다. (대소문자 + 숫자 조합)", // 국제화(i18n) 고려 필요
        groups = {UserValidationGroup.SignUp.class})
    @NotEmpty(groups = {UserValidationGroup.Login.class})
    private String password;

    private String token;

    // 생성자 정의를 하지 않으면, 기본생성자(파라미터가 없는 생성자)가 자동으로 만들어진다.

}

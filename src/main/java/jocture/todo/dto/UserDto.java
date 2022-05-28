package jocture.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 당장은 다른 생성자가 없을 때도, 정의해 둬야 나중에 오류 발생 가능성이 줄어든다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter //@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String id;
    private String username;
    private String email;
    private String password;
    private String token;

    // 생성자 정의를 하지 않으면, 기본생성자(파라미터가 없는 생성자)가 자동으로 만들어진다.

}

package jocture.todo.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseErrorDto {

    private String field;
    private Object rejectedValue;
    private String defaultMessage;
}

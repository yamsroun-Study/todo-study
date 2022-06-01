package jocture.todo.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 불변(immutable) 객체로 만드는게 좋다. -> setter 메소드는 없어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter // Serialize
public class ResponseDto<T> {
    // Generic 문자 (관례)
    // E - Element
    // N - Number
    // T - Type
    // K - Key
    // V - Value

    private static final String SUCCESS_CODE = "0000";
    private static final String SUCCESS_MESSAGE = "성공";

    private String code;
    private String message;
    private T result;

    // 정적 팩토리 메서드(Static Factory Method) 방식이 대체로 더 좋다.
    public static <T> ResponseDto<T> of(T result) {
        return new ResponseDto<>(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    }

    public static <T> ResponseDto<T> of(String code, String message, T result) {
        return new ResponseDto<>(code, message, result);
    }
}

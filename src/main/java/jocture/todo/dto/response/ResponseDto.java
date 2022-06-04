package jocture.todo.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 불변(immutable) 객체로 만드는게 좋다. -> setter 메소드는 없어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter // Serialize  : 객체 -> Json String으로 변환
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
    private ResponseResultDto<T> result; // 연관관계(Association) -> 집약관계(Aggregation)

    // 정적 팩토리 메서드(Static Factory Method) 방식이 대체로 더 좋다.
    // public static <T> ResponseDto<T> of(T result) {
    //     return new ResponseDto<>(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    // }

    public static <T> ResponseDto<T> of(ResponseResultDto<T> result) {
        // ResponseResultDto<T> result = new ResponseResultDto<>(); // 합성관계(Composition)
        return new ResponseDto<>(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    }

    // 메소드명이 같음, 파라미터 수 또는 파라미터 타입이 다르다 (메서드 시그니처가 다르다) -> Method Overloading
    // public static <T> ResponseDto<T> of(String code, String message, T result) {
    //     return new ResponseDto<>(code, message, result);
    // }
}

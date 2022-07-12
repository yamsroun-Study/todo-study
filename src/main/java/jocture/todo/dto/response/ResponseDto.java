package jocture.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jocture.todo.type.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

// 불변(immutable) 객체로 만드는게 좋다. -> setter 메소드는 없어야 한다.
@Getter // Serialize  : 객체 -> Json String으로 변환
public class ResponseDto<T> {
    // Generic 문자 (관례)
    // E - Element
    // N - Number
    // T - Type
    // K - Key
    // V - Value

    // private static final String SUCCESS_CODE = "0000";
    // private static final String SUCCESS_MESSAGE = "성공";

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseResultDto<T> result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseErrorDto> errors;

    private ResponseDto(String code, String message, ResponseResultDto<T> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    private ResponseDto(String code, String message, ResponseErrorDto error) {
        this.code = code;
        this.message = message;
        addError(error);
    }

    private ResponseDto(String code, String message, List<ResponseErrorDto> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    private ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // 정적 팩토리 메서드(Static Factory Method) 방식이 대체로 더 좋다.
    // pblic static <T> ResponseDto<T> of(T result) {
    //     return new ResponseDto<>(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    // }

    public static <T> ResponseDto<T> of(ResponseResultDto<T> result) {
        // ResponseResultDto<T> result = new ResponseResultDto<>();
        // return new ResponseDto<>("0000", "성공", result);
        return new ResponseDto<>(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.getMessage(), result);
    }

    // 메소드명이 같음, 파라미터 수 또는 파라미터 타입이 다르다 (메서드 시그니처가 다르다) -> Method Overloading
    public static <T> ResponseEntity<ResponseDto<T>> responseEntityOf(ResponseResultDto<T> result) {
        return ResponseEntity.ok(
            new ResponseDto<>(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.getMessage(), result)
        );
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityOf(ResponseCode responseCode) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus)
            .body(new ResponseDto<>(responseCode.code(), responseCode.getMessage()));
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityOf(ResponseCode responseCode, ResponseResultDto<T> result) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus)
            .body(new ResponseDto<>(responseCode.code(), responseCode.getMessage(), result));
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityOf(ResponseCode responseCode, ResponseErrorDto error) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus)
            .body(new ResponseDto<>(responseCode.code(), responseCode.getMessage(), error));
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityOf(ResponseCode responseCode, List<ResponseErrorDto> errors) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus)
            .body(new ResponseDto<>(responseCode.code(), responseCode.getMessage(), errors));
    }

    public static <T> ResponseDto<T> of(ResponseCode responseCode) {
        return new ResponseDto<>(responseCode.code(), responseCode.getMessage());
    }

    // 인스턴스 메서드 (vs. 스태틱 메서드)
    public void addError(ResponseErrorDto error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}

package jocture.todo.data.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ResponseCode {
    SUCCESS("0000", "성공", HttpStatus.OK),
    BAD_REQUEST("0400", "잘못된 요청", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("0401", "인증 실패", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("0403", "권한 없음", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR("0500", "내부 오류", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;

    @Getter
    private final String message;
    @Getter
    private final HttpStatus httpStatus;

    ResponseCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String code() {
        return code;
    }
}

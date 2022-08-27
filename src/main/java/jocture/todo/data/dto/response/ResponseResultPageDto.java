package jocture.todo.data.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE) // 클라이언트 측에서 직접 new를 이용해 객체 생성하는 것을 방지
@Getter
public class ResponseResultPageDto {

    private int page;
    private int rowSize;
    private int pageCount;

    // 정적 팩터리 메서드
    public static ResponseResultPageDto of(int page, int rowSize, int pageCount) {
        return new ResponseResultPageDto(page, rowSize, pageCount);
    }
}

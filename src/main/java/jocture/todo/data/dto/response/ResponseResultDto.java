package jocture.todo.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Collection;

@Getter
public class ResponseResultDto<T> {

    private final T data;
    private final int dataCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseResultPageDto dataPage;

    private ResponseResultDto(T data, int dataCount, ResponseResultPageDto dataPage) {
        this.data = data;
        this.dataCount = dataCount;
        this.dataPage = dataPage;
    }

    private ResponseResultDto(T data, int dataCount) {
        this.data = data;
        this.dataCount = dataCount;
    }

    public static <T> ResponseResultDto<T> of(T data) {
        return new ResponseResultDto<>(data, sizeOf(data));
        // 타입 추론(Type Inference)
    }

    public static <T> ResponseResultDto<T> of(T data, ResponseResultPageDto dataPage) {
        return new ResponseResultDto<>(data, sizeOf(data), dataPage);
        // 타입 추론(Type Inference)
    }

    private static <T> int sizeOf(T data) {
        if (data instanceof Collection) {
            return ((Collection<?>) data).size();
        }
        return 1;
    }

    public static <T> ResponseResultDto<T> of(T data, int dataCount, ResponseResultPageDto dataPage) {
        return new ResponseResultDto<>(data, dataCount, dataPage);
        // 타입 추론(Type Inference)
    }

}

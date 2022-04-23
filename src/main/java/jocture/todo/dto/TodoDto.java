package jocture.todo.dto;

import jocture.todo.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Builder // 다른 생성자가 없으면, 전체 파라미터 생성자 자동 생성
@NoArgsConstructor // HttpMessageConverter를 위해 필요
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder를 위해 필요
@Getter
@ToString
public class TodoDto {

    private Integer id;
    private String title;
    private boolean done;

    // 인스턴스(Instance) 메소스
    //   인스턴스 -> 객체 (new 클래스)
    // 정적(Static) 메소드
    public static TodoDto toDto(Todo todo) {
        return TodoDto.builder()
            .id(todo.getId())
            .title(todo.getTitle())
            .done(todo.isDone())
            .build();
    }

    public static List<TodoDto> toDtoList(List<Todo> todos) {
        return todos.stream()
            .map(TodoDto::toDto)
            .collect(Collectors.toList());

        // List<TodoDto> todoDtos = new ArrayList<>();
        // for (Todo todo: todos) {
        //     todoDtos.add(TodoDto.toDto(todo));
        // }
        // return todoDtos;
    }
}

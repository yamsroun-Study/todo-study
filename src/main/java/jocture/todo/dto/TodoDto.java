package jocture.todo.dto;

import jocture.todo.entity.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Builder
@Getter
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
            // .peek(t -> log.info("todo: {}", t)) // 동작 확인용
            .map(TodoDto::toDto)
            .collect(Collectors.toList());

        // List<TodoDto> todoDtos = new ArrayList<>();
        // for (Todo todo: todos) {
        //     todoDtos.add(TodoDto.toDto(todo));
        // }
        // return todoDtos;
    }
}

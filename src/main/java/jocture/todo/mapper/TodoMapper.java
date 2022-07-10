package jocture.todo.mapper;

import jocture.todo.config.MapStructConfig;
import jocture.todo.dto.*;
import jocture.todo.entity.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface TodoMapper {

    Todo toEntity(TodoDto dto);

    Todo toEntity(TodoCreateDto dto);

    Todo toEntity(TodoUpdateDto dto);

    Todo toEntity(TodoDeleteDto dto);

    TodoDto toDto(Todo entity);

    List<TodoDto> toDtoList(List<Todo> entity);
}

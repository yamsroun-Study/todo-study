package jocture.todo.data.mapper;

import jocture.todo.config.MapStructConfig;
import jocture.todo.data.dto.TodoDto;
import jocture.todo.data.entity.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface TodoMapper {

    Todo toEntity(TodoDto dto);

    TodoDto toDto(Todo entity);

    List<TodoDto> toDtoList(List<Todo> entity);
}

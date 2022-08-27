package jocture.todo.data.mapper;

import jocture.todo.config.MapStructConfig;
import jocture.todo.data.dto.UserDto;
import jocture.todo.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    // @Mapping(target = "memberName", source = "name")
    // @Mapping(target = "memberId", expression = "java(member.getId())")
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(target = "password", ignore = true)
    UserDto toDto(User entity);
}

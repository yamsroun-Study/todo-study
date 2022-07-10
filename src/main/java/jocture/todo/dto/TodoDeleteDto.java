package jocture.todo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class TodoDeleteDto {

    @NotNull
    private Integer id;
}

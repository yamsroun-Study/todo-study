package jocture.todo.dto;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class TodoUpdateDto {

    @NotNull
    private Integer id;

    @NotNull @NotEmpty @NotBlank
    private String title;

    private boolean done;
}

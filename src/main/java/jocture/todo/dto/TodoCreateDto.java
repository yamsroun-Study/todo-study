package jocture.todo.dto;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor // HttpMessageConverter를 위해 필요
@AllArgsConstructor
@Getter
@ToString
public class TodoCreateDto {

    @NotNull @NotEmpty @NotBlank
    private String title;
}

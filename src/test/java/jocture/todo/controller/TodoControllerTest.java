package jocture.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jocture.todo.data.dto.TodoDto;
import jocture.todo.data.mapper.TodoMapper;
import jocture.todo.data.type.ResponseCode;
import jocture.todo.service.TodoService;
import jocture.todo.web.controller.TodoController;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest({TodoController.class, TodoMapper.class})
class TodoControllerTest {

    @Autowired MockMvc mvc;
    @MockBean TodoService todoService;
    @Autowired TodoMapper todoMapper;
    @Autowired ObjectMapper objectMapper;

    @Test
    void createTodo() throws Exception {
        // Given
        String url = "/todo/v7";
        // Java 10에서 var 도입
        // -> 지역 변수에만 사용 가능
        // -> 컴파일러가 타입 추론(Type Inference)이 가능한 경우 사용 가능
        var dto = TodoDto.builder()
            .title("스프링 공부하기")
            .build();
        String body = objectMapper.writeValueAsString(dto); // Serialize : 객체 -> JSON 스트링으로 변환

        // When && Then
        mvc.perform(post(url).content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", Matchers.containsString(ResponseCode.SUCCESS.code())));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void createTodo_ValidError(String title) throws Exception {
        // Given
        String url = "/todo/v7";
        var dto = TodoDto.builder()
            .title(title)
            .build();
        String body = objectMapper.writeValueAsString(dto);

        // When && Then
        mvc.perform(post(url).content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", Matchers.containsString(ResponseCode.BAD_REQUEST.code())));
    }

    @Test
    void updateTodo() throws Exception {
        // Given
        String url = "/todo/v7";
        var dto = TodoDto.builder()
            .id(1)
            .title("스프링 공부하기")
            .done(true)
            .build();
        String body = objectMapper.writeValueAsString(dto);

        // When && Then
        mvc.perform(put(url).content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", Matchers.containsString(ResponseCode.SUCCESS.code())));
    }

    @ParameterizedTest
    @CsvSource(value = {":자바:true", "1::false", "::true"}, delimiter = ':')
    void updateTodo_ValidError(Integer id, String title, boolean done) throws Exception {
        // Given
        String url = "/todo/v7";
        var dto = TodoDto.builder()
            .id(id)
            .title(title)
            .done(done)
            .build();
        String body = objectMapper.writeValueAsString(dto);

        // When && Then
        mvc.perform(put(url).content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", Matchers.containsString(ResponseCode.BAD_REQUEST.code())));

    }

    @Test
    void deleteTodo() throws Exception {
        // Given
        String url = "/todo/v7";
        var dto = TodoDto.builder()
            .id(1)
            .build();
        String body = objectMapper.writeValueAsString(dto);

        // When && Then
        mvc.perform(delete(url).content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", Matchers.containsString(ResponseCode.SUCCESS.code())));
    }

    @ParameterizedTest
    @NullSource
    void deleteTodo_ValidError(Integer id) throws Exception {
        // Given
        String url = "/todo/v7";
        var dto = TodoDto.builder()
            .id(id)
            .build();
        String body = objectMapper.writeValueAsString(dto);

        // When && Then
        mvc.perform(delete(url).content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", Matchers.containsString(ResponseCode.BAD_REQUEST.code())));
    }
}
package jocture.todo.controller.advice;

import jocture.todo.dto.response.ResponseDto;
import jocture.todo.exception.ApplicationException;
import jocture.todo.type.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TodoControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> badRequestHandler(Exception e) {
        log.error("badRequestHandler ->", e);
        return ResponseEntity.badRequest().body("ERROR");
    }

    @ExceptionHandler//(ApplicationException.class)
    public ResponseEntity<?> applicationExceptionHandler(ApplicationException e) {
        log.error("applicationExceptionHandler ->", e);
        String message1 = e.getMessage();
        String message2 = e.getMessage() + "2222";
        return ResponseDto.<String>responseEntityOf(ResponseCode.BAD_REQUEST, message1);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception e) {
        log.error("globalExceptionHandler ->", e);
        return ResponseEntity.internalServerError().body("ERROR");
    }
}

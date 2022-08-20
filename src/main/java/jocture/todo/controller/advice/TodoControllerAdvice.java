package jocture.todo.controller.advice;

import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseErrorDto;
import jocture.todo.exception.ApplicationException;
import jocture.todo.exception.AuthenticationProblemException;
import jocture.todo.type.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class TodoControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<?> bindExceptionHandler(BindException ex) {
        log.error("bindExceptionHandler ->", ex);
        BindingResult bindingResult = ex.getBindingResult();
        // List<ObjectError> allErrors = bindingResult.getAllErrors();
        // allErrors.forEach(objectError -> {
        //     log.debug(">>> getObjectName    : {}", objectError.getObjectName());
        //     log.debug(">>> getArguments     : {}", objectError.getArguments());
        //     log.debug(">>> getDefaultMessage: {}", objectError.getDefaultMessage());
        // });
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // 중하수
        // var errors = new ArrayList<ResponseErrorDto>();
        // fieldErrors.forEach(fieldError -> {
        //     var error = new ResponseErrorDto(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        //     errors.add(error);
        // });
        // 중수 이상
        var errors = fieldErrors.stream()
            .map(fieldError -> new ResponseErrorDto(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
        return ResponseDto.<String>responseEntityOf(ResponseCode.BAD_REQUEST, errors);
    }

    @ExceptionHandler
    public ResponseEntity<?> applicationExceptionHandler(ApplicationException e) {
        log.error("applicationExceptionHandler ->", e);
        ResponseErrorDto responseError = new ResponseErrorDto("", "", e.getMessage());
        return ResponseDto.responseEntityOf(ResponseCode.INTERNAL_SERVER_ERROR, responseError);
    }

    //@ExceptionHandler({LoginFailException.class, NoAuthenticationException.class})
    //@ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ResponseEntity<?> authenticationProblemExceptionHandler(AuthenticationProblemException e) {
        log.error("authenticationProblemExceptionHandler -> {}:{}", e.getClass().getSimpleName(), e.getMessage());
        ResponseErrorDto responseError = new ResponseErrorDto("", "", e.getMessage());
        return ResponseDto.responseEntityOf(ResponseCode.UNAUTHORIZED, responseError);
    }

    @ExceptionHandler
    public ResponseEntity<?> globalExceptionHandler(Exception e) {
        log.error("globalExceptionHandler ->", e);
        ResponseErrorDto responseError = new ResponseErrorDto("", "", e.getMessage());
        return ResponseDto.responseEntityOf(ResponseCode.INTERNAL_SERVER_ERROR, responseError);
    }
}

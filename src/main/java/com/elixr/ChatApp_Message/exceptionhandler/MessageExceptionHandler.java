package com.elixr.ChatApp_Message.exceptionhandler;

import com.elixr.ChatApp_Message.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class MessageExceptionHandler {

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<Response> handleUserException(MessageException messageException){
        List<String> errors = new ArrayList<>();
        errors.add(messageException.getMessage());
        log.error(messageException.getMessage());
        return new ResponseEntity<>(new Response(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<Response> handleUserNotFoundException(MessageNotFoundException messageNotFoundException){
        List<String> errors = new ArrayList<>();
        errors.add(messageNotFoundException.getMessage());
        log.error(messageNotFoundException.getMessage());
        return new ResponseEntity<>(new Response(errors),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        List<String> errors = new ArrayList<>();
        errors.add(accessDeniedException.getMessage());
        log.error(accessDeniedException.getMessage());
        return new ResponseEntity<>(new Response(errors),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception exception){
        List<String> errors = new ArrayList<>();
        errors.add(exception.getMessage());
        log.error(exception.getMessage());
        return new ResponseEntity<>(new Response(errors),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

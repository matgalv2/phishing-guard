package io.github.g4lowy.phishingguard.configuration;

import io.github.g4lowy.phishingguard.common.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.LinkedList;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationExceptions(WebExchangeBindException webExchangeBindException) {

        LinkedList<String> messages = new LinkedList<>();

        webExchangeBindException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            messages.add(fieldName + ": " + errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageDto(messages));
    }
}
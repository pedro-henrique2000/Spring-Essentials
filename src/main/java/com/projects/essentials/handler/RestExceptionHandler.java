package com.projects.essentials.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.projects.essentials.exceptions.BadRequestDetailsException;
import com.projects.essentials.exceptions.BadRequestException;
import com.projects.essentials.exceptions.ExceptionDetails;
import com.projects.essentials.exceptions.ValidationExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String BAD_REQUEST_EXCEPTION = "Bad Request Exception";

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<BadRequestDetailsException> handleBadRequestException(final BadRequestException exception) {
        BadRequestDetailsException detailsException = BadRequestDetailsException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .title(BAD_REQUEST_EXCEPTION)
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .build();

        return ResponseEntity.badRequest().body(detailsException);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String field = fieldErrors.stream().map(f -> f.getField()).collect(Collectors.joining(", "));
        String messages = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.joining(", "));

        ValidationExceptionDetails validationExceptionDetails = ValidationExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .title(BAD_REQUEST_EXCEPTION)
                .details(exception.getCause().getMessage())
                .developerMessage(exception.getClass().getName())
                .fieldMessage(messages)
                .fields(field)
                .build();

        return ResponseEntity.badRequest().body(validationExceptionDetails);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .title(ex.getCause().getMessage())
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();

        System.out.println(status);

        return new ResponseEntity(exceptionDetails, headers, status);
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<BadRequestDetailsException> handleBadRequestException(final InvalidFormatException exception) {
        BadRequestDetailsException detailsException = BadRequestDetailsException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .title(BAD_REQUEST_EXCEPTION)
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .build();

        return ResponseEntity.badRequest().body(detailsException);
    }

}

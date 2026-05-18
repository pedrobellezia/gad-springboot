package com.example.gad.exceptions;

import com.example.gad.services.exceptions.DataBindingViolationException;
import com.example.gad.services.exceptions.ObjectNotFoundException;

import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Pattern UK_REDATOR_USUARIO_EMAIL_PATTERN =
            Pattern.compile("UK_REDATOR_USUARIO_EMAIL", Pattern.CASE_INSENSITIVE);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação. Verifique os campos enviados."
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        log.warn("Erro de validação: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {

        log.warn("Violação de restrição: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Falha na validação"
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException ex,
            WebRequest request) {

        log.warn("Objeto não encontrado: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(DataBindingViolationException.class)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException ex,
            WebRequest request) {

        log.warn("Violação de vínculo de dados: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            WebRequest request) {

        String causeMessage = ex.getMostSpecificCause().getMessage();
        String message = causeMessage != null ? causeMessage : ex.getMessage();

        if (message != null && UK_REDATOR_USUARIO_EMAIL_PATTERN.matcher(message).find()) {
            message = "Email ja cadastrado para este redator.";
        }

        log.error("Violação de integridade de dados: {}", message);

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                message
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception ex,
            WebRequest request) {

        log.error("Erro inesperado", ex);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado"
        );
    }

    private ResponseEntity<Object> buildErrorResponse(
            HttpStatus status,
            String message) {

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message
        );

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }
}
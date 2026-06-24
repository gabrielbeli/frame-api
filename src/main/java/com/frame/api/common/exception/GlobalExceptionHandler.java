package com.frame.api.common.exception;

import com.frame.api.common.response.ApiErrorResponse;
import com.frame.api.common.response.ApiFieldError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiErrorResponse handleResourceNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ApiErrorResponse handleConflict(
            ConflictException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;

        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<ApiFieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiFieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorResponse handleInvalidJson(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Malformed JSON request or invalid value",
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiErrorResponse handleBadCredentials(
            BadCredentialsException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Invalid credentials",
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleGenericException(
            Exception exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "An unexpected error occurred",
                request.getRequestURI(),
                List.of()
        );
    }
}

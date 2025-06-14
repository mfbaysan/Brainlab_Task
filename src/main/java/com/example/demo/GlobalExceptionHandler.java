package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        if ("No operands provided".equals(msg)) {
            return Map.of(
                    "error",   "MISSING_OPERANDS",
                    "message", msg
            );
        } else {
            return Map.of(
                    "error",   "INVALID_OPERANDS",
                    "message", msg
            );
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingParam(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return Map.of(
                "error",   "MISSING_PARAMETER",
                "message", "Required parameter '" + name + "' is missing",
                "expected", "Use ?"+ name + "=<comma-separated numbers>"
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex) {
        return Map.of(
                "error",   "INTERNAL_SERVER_ERROR",
                "message", ex.getMessage()
        );
    }
}

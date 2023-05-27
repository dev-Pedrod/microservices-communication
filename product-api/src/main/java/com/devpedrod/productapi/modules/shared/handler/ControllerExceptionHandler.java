package com.devpedrod.productapi.modules.shared.handler;

import com.devpedrod.productapi.modules.shared.DTO.ExceptionResponse;
import com.devpedrod.productapi.modules.shared.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ExceptionResponse> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(NOT_FOUND).body(ExceptionResponse.builder()
                .timeStamp(now())
                .status(NOT_FOUND)
                .statusCode(NOT_FOUND.value())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build());
    }
}

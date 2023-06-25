package com.devpedrod.productapi.modules.shared.exceptions;

public class ValidationException extends RuntimeException{
    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

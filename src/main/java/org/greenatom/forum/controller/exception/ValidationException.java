package org.greenatom.forum.controller.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(ExceptionMessage message) {
        super(message.getMessage());
    }
}

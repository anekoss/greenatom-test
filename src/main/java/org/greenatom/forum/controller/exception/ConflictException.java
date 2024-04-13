package org.greenatom.forum.controller.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
    }
}

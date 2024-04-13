package org.greenatom.forum.controller.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(ExceptionMessage message) {
        super(message.getMessage());
    }
}

package org.greenatom.forum.controller.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    INVALID_INPUT_MESSAGE("Invalid input"),
    VALIDATION_EXCEPTION_MESSAGE("Validation exception"),
    INVALID_ID_SUPPLIED_MESSAGE("Invalid ID supplied"),
    TOPIC_NOT_FOUND_MESSAGE("Topic not found"),
    INVALID_TOPIC_ID_MESSAGE("Invalid topic ID"),
    MESSAGE_NOT_FOUND_MESSAGE("Message not found"),
    USER_NOT_FOUND_MESSAGE("User with this username not found"),
    USER_ALREADY_REGISTER("User with this username already exists");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}

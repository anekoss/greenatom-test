package org.greenatom.forum.controller;

import org.greenatom.forum.controller.exception.ConflictException;
import org.greenatom.forum.controller.exception.ForbiddenException;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Некорректные параметры запроса")
    public ErrorMessage validationException(ValidationException exception, WebRequest webRequest) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Недостаточно прав для выполнения запроса")
    public ErrorMessage forbiddenException(ForbiddenException exception, WebRequest webRequest) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Указанные топик или сообщение не найдены")
    public ErrorMessage notFoundException(NotFoundException exception, WebRequest webRequest) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Проблема с авторизацией/регистрацией пользователя")
    public ErrorMessage conflictUserException(ConflictException exception, WebRequest webRequest) {
        return new ErrorMessage(exception.getMessage());
    }
}

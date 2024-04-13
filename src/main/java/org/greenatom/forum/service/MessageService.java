package org.greenatom.forum.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import model.Message;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;

public interface MessageService {

    Message createMessage(UUID topicId, Message message) throws ValidationException, NotFoundException;

    Message updateMessage(UUID topicId, Message message) throws ValidationException, NotFoundException;

    Message updateUserMessage(@NotNull UUID topicId, @NotNull Message message, @NotBlank String username)
        throws NotFoundException, ValidationException;

    void deleteMessage(UUID messageId) throws NotFoundException, ValidationException;

    void deleteUserMessage(@NotNull UUID messageId, @NotBlank String username)
        throws NotFoundException, ValidationException;
}

package org.greenatom.forum.service.imdb;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import model.Message;
import org.greenatom.forum.controller.exception.ExceptionMessage;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.greenatom.forum.repository.MessageRepository;
import org.greenatom.forum.service.MessageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InMemoryMessageService implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(@NotNull UUID topicId, @NotNull Message message)
        throws ValidationException, NotFoundException {
        return messageRepository.add(message, topicId);
    }

    @Override
    public Message updateMessage(@NotNull UUID topicId, @NotNull Message message)
        throws ValidationException, NotFoundException {
        return messageRepository.update(message, topicId);
    }

    @Override
    public Message updateUserMessage(@NotNull UUID topicId, @NotNull Message message, @NotBlank String username)
        throws NotFoundException, ValidationException {
        Message prevMessage = messageRepository.findById(message.getId());
        if (!username.equals(prevMessage.getAuthor())) {
            throw new ValidationException(ExceptionMessage.VALIDATION_EXCEPTION_MESSAGE);
        }
        return updateMessage(topicId, message);
    }

    @Override
    public void deleteMessage(@NotNull UUID messageId) throws NotFoundException, ValidationException {
        messageRepository.findById(messageId);
        messageRepository.delete(messageId);
    }

    @Override
    public void deleteUserMessage(@NotNull UUID messageId, @NotBlank String username)
        throws NotFoundException, ValidationException {
        Message prevMessage = messageRepository.findById(messageId);
        if (!prevMessage.getAuthor().equals(username)) {
            throw new ValidationException(ExceptionMessage.VALIDATION_EXCEPTION_MESSAGE);
        }
        messageRepository.delete(messageId);
    }

}

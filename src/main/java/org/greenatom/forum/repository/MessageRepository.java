package org.greenatom.forum.repository;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import model.Message;
import org.greenatom.forum.controller.exception.ExceptionMessage;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.greenatom.forum.table.MessageTable;
import org.greenatom.forum.table.TopicMessageTable;
import org.greenatom.forum.table.TopicTable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageRepository {
    private final MessageTable messageTable;
    private final TopicTable topicTable;
    private final TopicMessageTable topicMessageTable;

    public Message add(@NotNull Message message, @NotNull UUID topicId) throws ValidationException, NotFoundException {
        if (!topicMessageTable.existByTopicId(topicId)) {
            throw new NotFoundException(ExceptionMessage.TOPIC_NOT_FOUND_MESSAGE);
        }
        Message newMessage = messageTable.insert(message);
        topicMessageTable.insertMessage(topicId, newMessage.getId());
        return message;
    }

    public Message delete(@NotNull UUID messageId) throws ValidationException, NotFoundException {
        UUID topicId = topicMessageTable.findByMessageId(messageId);
        List<UUID> messageIds = topicMessageTable.deleteMessage(topicId, messageId);
        if (messageIds.isEmpty()) {
            topicTable.delete(topicId);
        }
        return messageTable.delete(messageId);
    }

    public Message update(@NotNull Message message, @NotNull UUID topicId)
        throws NotFoundException, ValidationException {
        if (!topicMessageTable.exist(topicId, message.getId())) {
            throw new ValidationException(ExceptionMessage.VALIDATION_EXCEPTION_MESSAGE);
        }
        return messageTable.update(message);
    }

    public Message findById(@NotNull UUID id) throws NotFoundException {
        return messageTable.findById(id);
    }

}

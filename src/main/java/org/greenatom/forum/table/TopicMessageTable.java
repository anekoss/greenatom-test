package org.greenatom.forum.table;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.greenatom.forum.controller.exception.ValidationException;
import org.springframework.stereotype.Component;
import static org.greenatom.forum.controller.exception.ExceptionMessage.VALIDATION_EXCEPTION_MESSAGE;

@Component
@NoArgsConstructor
public class TopicMessageTable {
    private final Map<UUID, List<UUID>> topicMessages = new TreeMap<>(UUID::compareTo);
    private final Map<UUID, UUID> messageTopic = new HashMap<>();

    public List<UUID> insertTopic(@NotNull UUID topicId, @NotNull UUID messageId) throws ValidationException {
        if (existByTopicId(topicId) || existByMessageId(messageId)) {
            throw new ValidationException(VALIDATION_EXCEPTION_MESSAGE);
        }
        messageTopic.put(messageId, topicId);
        List<UUID> messageIds = new ArrayList<>();
        messageIds.add(messageId);
        topicMessages.put(topicId, messageIds);
        return messageIds;
    }

    public List<UUID> insertMessage(@NotNull UUID topicId, @NotNull UUID messageId) throws ValidationException {
        if (!existByTopicId(topicId) || existByMessageId(messageId)) {
            throw new ValidationException(VALIDATION_EXCEPTION_MESSAGE);
        }
        messageTopic.put(messageId, topicId);
        List<UUID> messageIds = topicMessages.get(topicId);
        messageIds.add(messageId);
        topicMessages.put(topicId, messageIds);
        return messageIds;
    }

    public List<UUID> deleteMessage(@NotNull UUID topicId, @NotNull UUID messageId) throws ValidationException {
        if (!exist(topicId, messageId)) {
            throw new ValidationException(VALIDATION_EXCEPTION_MESSAGE);
        }
        List<UUID> messageIds = topicMessages.get(topicId);
        messageIds.remove(messageId);
        messageTopic.remove(messageId);
        if (messageIds.isEmpty()) {
            topicMessages.remove(topicId);
        } else {
            topicMessages.put(topicId, messageIds);
        }
        return messageIds;
    }

    public List<UUID> deleteTopic(@NotNull UUID topicId) throws ValidationException {
        if (!existByTopicId(topicId)) {
            throw new ValidationException(VALIDATION_EXCEPTION_MESSAGE);
        }
        List<UUID> messageIds = topicMessages.remove(topicId);
        messageIds.forEach(messageTopic::remove);
        return messageIds;
    }

    public List<UUID> findByTopicId(@NotNull UUID topicId) throws ValidationException {
        if (!existByTopicId(topicId)) {
            throw new ValidationException(VALIDATION_EXCEPTION_MESSAGE);
        }
        return topicMessages.get(topicId);
    }

    public UUID findByMessageId(@NotNull UUID messageId) throws ValidationException {
        if (!existByMessageId(messageId)) {
            throw new ValidationException(VALIDATION_EXCEPTION_MESSAGE);
        }
        return messageTopic.get(messageId);
    }

    public Map<UUID, List<UUID>> findAll() {
        return topicMessages;
    }

    public boolean existByMessageId(@NotNull UUID messageId) {
        return messageTopic.get(messageId) != null;
    }

    public boolean existByTopicId(@NotNull UUID topicId) {
        return topicMessages.get(topicId) != null;
    }

    public boolean exist(@NotNull UUID topicId, @NotNull UUID messageId) {
        return messageTopic.get(messageId) == topicId;
    }

}

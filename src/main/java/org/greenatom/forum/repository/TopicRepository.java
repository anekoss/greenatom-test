package org.greenatom.forum.repository;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import model.Message;
import model.Topic;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.greenatom.forum.table.MessageTable;
import org.greenatom.forum.table.TopicMessageTable;
import org.greenatom.forum.table.TopicTable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicRepository {
    private final TopicTable topicTable;
    private final MessageTable messageTable;
    private final TopicMessageTable topicMessageTable;

    public Topic add(@NotNull Topic topic, @NotNull Message message) throws ValidationException {
        Topic newTopic = topicTable.insert(topic);
        Message newMessage = messageTable.insert(message);
        topicMessageTable.insertTopic(newTopic.getId(), newMessage.getId());
        return newTopic;
    }

    public Topic update(@NotNull Topic topic) throws NotFoundException {
        return topicTable.update(topic);
    }

    public Topic delete(@NotNull UUID topicId) throws ValidationException, NotFoundException {
        List<UUID> messageIds = topicMessageTable.deleteTopic(topicId);
        for (UUID id : messageIds) {
            messageTable.delete(id);
        }
        return topicTable.delete(topicId);
    }

    public List<Topic> findAll(long skip, long limit) {
        return topicTable.findAll().stream().skip(skip).limit(limit).toList();
    }

    public Topic findById(@NotNull UUID id) throws NotFoundException {
        return topicTable.findById(id);
    }

    public List<Message> findMessagesByTopicId(@NotNull UUID topicId, long skip, long limit)
        throws ValidationException, NotFoundException {
        List<UUID> messageIds = topicMessageTable.findByTopicId(topicId)
                                                 .stream()
                                                 .skip(skip)
                                                 .limit(limit)
                                                 .toList();
        List<Message> messages = new ArrayList<>();
        for (UUID id : messageIds) {
            messages.add(messageTable.findById(id));
        }
        return messages;
    }

    public int findTopicsCount() {
        return topicMessageTable.findAll().size();
    }

    public int findMessagesCountByTopicId(@NotNull UUID topicId) throws ValidationException {
        return topicMessageTable.findByTopicId(topicId).size();
    }
}

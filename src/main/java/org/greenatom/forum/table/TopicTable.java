package org.greenatom.forum.table;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import lombok.NoArgsConstructor;
import model.Topic;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.springframework.stereotype.Component;
import static org.greenatom.forum.controller.exception.ExceptionMessage.TOPIC_NOT_FOUND_MESSAGE;

@Component
@NoArgsConstructor
public class TopicTable {
    private final Map<UUID, Topic> topics = new TreeMap<>(UUID::compareTo);

    public Topic insert(@NotNull Topic topic) {
        UUID id = UUID.randomUUID();
        topic.setId(id);
        topics.put(id, topic);
        return topic;
    }

    public Topic delete(@NotNull UUID id) throws NotFoundException {
        if (!existById(id)) {
            throw new NotFoundException(TOPIC_NOT_FOUND_MESSAGE);
        }
        return topics.remove(id);
    }

    public Topic update(@NotNull Topic topic) throws NotFoundException {
        if (!existById(topic.getId())) {
            throw new NotFoundException(TOPIC_NOT_FOUND_MESSAGE);
        }
        topics.put(topic.getId(), topic);
        return topic;
    }

    public List<Topic> findAll() {
        return topics.values().stream().toList();
    }

    public boolean existById(@NotNull UUID id) {
        return topics.get(id) != null;
    }

    public Topic findById(@NotNull UUID id) throws NotFoundException {
        if (!existById(id)) {
            throw new NotFoundException(TOPIC_NOT_FOUND_MESSAGE);
        }
        return topics.get(id);
    }
}

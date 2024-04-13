package org.greenatom.forum.table;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import model.Message;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.springframework.stereotype.Component;
import static org.greenatom.forum.controller.exception.ExceptionMessage.MESSAGE_NOT_FOUND_MESSAGE;

@Component
public class MessageTable {
    private final Map<UUID, Message> messages = new TreeMap<>(UUID::compareTo);

    public Message insert(@NotNull Message message) {
        UUID id = UUID.randomUUID();
        message.setId(id);
        messages.put(id, message);
        return message;
    }

    public Message delete(@NotNull UUID id) throws NotFoundException {
        if (!existById(id)) {
            throw new NotFoundException(MESSAGE_NOT_FOUND_MESSAGE);
        }
        return messages.remove(id);
    }

    public Message update(@NotNull Message message) throws NotFoundException {
        UUID id = message.getId();
        if (!existById(id)) {
            throw new NotFoundException(MESSAGE_NOT_FOUND_MESSAGE);
        }
        messages.put(id, message);
        return message;
    }

    public Message findById(@NotNull UUID id) throws NotFoundException {
        if (!existById(id)) {
            throw new NotFoundException(MESSAGE_NOT_FOUND_MESSAGE);
        }
        return messages.get(id);
    }

    public List<Message> findAll() {
        return messages.values().stream().toList();
    }

    private boolean existById(@NotNull UUID id) {

        return messages.get(id) != null;
    }

}

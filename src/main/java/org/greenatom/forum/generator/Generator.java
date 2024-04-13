package org.greenatom.forum.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import model.Message;
import model.NewTopic;
import model.TopicWithMessages;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.greenatom.forum.service.MessageService;
import org.greenatom.forum.service.TopicService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Generator {
    private static final String FILE_PATH = "data.json";
    private static final int TOPIC_COUNT = 50;
    private static final int MESSAGE_COUNT = 10;
    private static final String AUTHOR_USERNAME = "username";
    private final TopicService topicService;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @EventListener(ContextRefreshedEvent.class)
    public void generate() throws ValidationException, NotFoundException, IOException {
        for (int i = 0; i < TOPIC_COUNT; i++) {
            Message message = new Message().author(AUTHOR_USERNAME).text("text").created(OffsetDateTime.now());
            NewTopic newTopic = new NewTopic().topicName("topic" + i).message(message);
            TopicWithMessages topicWithMessages = topicService.createTopic(newTopic, 0, 1);
            for (int j = 0; j < MESSAGE_COUNT; j++) {
                Message newMessage =
                    new Message().author(AUTHOR_USERNAME).text("j" + j).created(OffsetDateTime.now());
                messageService.createMessage(topicWithMessages.getId(), newMessage);
            }
        }
        List<TopicWithMessages> topicWithMessagesList = topicService.getAllTopicWithMessages(0, TOPIC_COUNT);
        objectMapper.writeValue(new File(FILE_PATH), topicWithMessagesList);
    }
}

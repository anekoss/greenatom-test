package org.greenatom.forum.controller;

import api.TopicApi;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import model.Message;
import model.NewTopic;
import model.Topic;
import model.TopicWithMessages;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.greenatom.forum.service.MessageService;
import org.greenatom.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import static org.greenatom.forum.auth.service.UserDetailsServiceImpl.getUsernameCurrentUser;
import static org.greenatom.forum.auth.service.UserDetailsServiceImpl.isAdmin;

@RestController
@RequiredArgsConstructor
public class TopicController implements TopicApi {
    private final TopicService topicService;
    private final MessageService messageService;

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT')")
    public ResponseEntity<TopicWithMessages> createTopic(
        NewTopic newTopic,
        @Value("#{@page.number}") Long pageNumber,
        @Value("#{page.size}") Long pageSize
    ) throws ValidationException, NotFoundException {
        TopicWithMessages topicWithMessages = topicService.createTopic(newTopic, pageNumber, pageSize);
        return ResponseEntity.ok(topicWithMessages);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TopicWithMessages>> updateTopic(
        Topic topic,
        @Value("#{@page.number}") Long pageNumber,
        @Value("#{page.size}") Long pageSize
    ) throws ValidationException, NotFoundException {
        List<TopicWithMessages> topicWithMessagesList = topicService.updateTopic(topic, pageNumber, pageSize);
        return ResponseEntity.ok(topicWithMessagesList);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<List<Topic>> listAllTopics(
        @Value("#{@page.number}") Long pageNumber,
        @Value("#{page.size}") Long pageSize
    ) {
        List<Topic> topics = topicService.getAllTopic(pageNumber, pageSize);
        return ResponseEntity.ok(topics);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<TopicWithMessages> listTopicMessages(
        UUID topicId,
        @Value("#{@page.number}") Long pageNumber,
        @Value("#{page.size}") Long pageSize
    ) throws NotFoundException, ValidationException {
        TopicWithMessages topicWithMessages = topicService.getTopicWithMessagesByTopicId(topicId, pageNumber, pageSize);
        return ResponseEntity.ok(topicWithMessages);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTopic(UUID topicId) throws ValidationException, NotFoundException {
        topicService.deleteTopic(topicId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT')")
    public ResponseEntity<TopicWithMessages> createMessage(
        UUID topicId, Message message,
        @Value("#{@page.number}") Long pageNumber,
        @Value("#{page.size}") Long pageSize
    ) throws ValidationException, NotFoundException {
        messageService.createMessage(topicId, message);
        TopicWithMessages topicWithMessages = topicService.getTopicWithMessagesByTopicId(topicId, pageNumber, pageSize);
        return ResponseEntity.ok(topicWithMessages);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<List<TopicWithMessages>> updateMessage(
        UUID topicId, Message message,
        @Value("#{@page.number}") Long pageNumber,
        @Value("#{page.size}") Long pageSize
    ) throws ValidationException, NotFoundException {
        if (isAdmin()) {
            messageService.updateMessage(topicId, message);
        } else {
            messageService.updateUserMessage(topicId, message, getUsernameCurrentUser());
        }
        List<TopicWithMessages> topicWithMessagesList = topicService.getAllTopicWithMessages(pageNumber, pageSize);
        return ResponseEntity.ok(topicWithMessagesList);
    }
}

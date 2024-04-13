package org.greenatom.forum.service.imdb;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import model.Message;
import model.NewTopic;
import model.Topic;
import model.TopicWithMessages;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.greenatom.forum.repository.TopicRepository;
import org.greenatom.forum.service.TopicService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InMemoryTopicService implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public TopicWithMessages createTopic(@NotNull NewTopic newTopic, long pageNumber, long pageSize)
        throws ValidationException, NotFoundException {
        Topic topic = new Topic().name(newTopic.getTopicName()).created(OffsetDateTime.now());
        Message message = newTopic.getMessage();
        topicRepository.add(topic, message);
        return getTopicWithMessagesByTopicId(topic.getId(), pageNumber, pageSize);
    }

    public List<TopicWithMessages> updateTopic(@NotNull Topic topic, long pageNumber, long pageSize)
        throws NotFoundException, ValidationException {
        topicRepository.update(topic);
        return getAllTopicWithMessages(pageNumber, pageSize);
    }

    public List<TopicWithMessages> updateUserTopic(@NotNull Topic topic, long pageNumber, long pageSize)
        throws NotFoundException, ValidationException {
        topicRepository.update(topic);
        return getAllTopicWithMessages(pageNumber, pageSize);
    }

    @Override
    public List<Topic> getAllTopic(long pageNumber, long pageSize) {
        int countTopics = topicRepository.findTopicsCount();
        long skip = pageNumber * (pageSize - 1);
        return skip >= countTopics ? List.of() : topicRepository.findAll(skip, pageSize);
    }

    @Override
    public void deleteTopic(@NotNull UUID topicId) throws ValidationException, NotFoundException {
        topicRepository.delete(topicId);
    }

    @Override
    public List<TopicWithMessages> getAllTopicWithMessages(long pageNumber, long pageSize)
        throws ValidationException, NotFoundException {
        List<Topic> topics = getAllTopic(pageNumber, pageSize);
        List<TopicWithMessages> topicWithMessagesList = new ArrayList<>();
        for (Topic topic : topics) {
            TopicWithMessages topicWithMessages = getTopicWithMessagesByTopicId(topic.getId(), pageNumber, pageSize);
            topicWithMessagesList.add(topicWithMessages);
        }
        return topicWithMessagesList;
    }

    @Override
    public TopicWithMessages getTopicWithMessagesByTopicId(@NotNull UUID topicId, long pageNumber, long pageSize)
        throws ValidationException, NotFoundException {
        int countMessages = topicRepository.findMessagesCountByTopicId(topicId);
        long skip = pageNumber * (pageSize - 1);
        List<Message> messages =
            skip >= countMessages ? List.of() : topicRepository.findMessagesByTopicId(topicId, skip, pageSize);
        Topic topic = topicRepository.findById(topicId);
        return new TopicWithMessages(topicId, topic.getName(), messages);
    }

}

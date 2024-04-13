package org.greenatom.forum.service;

import java.util.List;
import java.util.UUID;
import model.NewTopic;
import model.Topic;
import model.TopicWithMessages;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;

public interface TopicService {

    TopicWithMessages createTopic(NewTopic newTopic, long pageNumber, long pageSize)
        throws ValidationException, NotFoundException;

    List<TopicWithMessages> updateTopic(Topic topic, long pageNumber, long pageSize)
        throws NotFoundException, ValidationException;

    List<Topic> getAllTopic(long pageNumber, long pageSize);

    List<TopicWithMessages> getAllTopicWithMessages(long pageNumber, long pageSize)
        throws ValidationException, NotFoundException;

    TopicWithMessages getTopicWithMessagesByTopicId(UUID topicId, long pageNumber, long pageSize)
        throws ValidationException, NotFoundException;

    void deleteTopic(UUID topicId) throws ValidationException, NotFoundException;

}

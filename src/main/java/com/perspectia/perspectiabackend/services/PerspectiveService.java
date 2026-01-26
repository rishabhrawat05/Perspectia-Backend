package com.perspectia.perspectiabackend.services;

import com.perspectia.perspectiabackend.exceptions.PerspectiveNotFoundException;
import com.perspectia.perspectiabackend.exceptions.TopicNotFoundException;
import com.perspectia.perspectiabackend.exceptions.UserNotFoundException;
import com.perspectia.perspectiabackend.models.Perspective;
import com.perspectia.perspectiabackend.repositories.PerspectiveRepository;
import com.perspectia.perspectiabackend.repositories.TopicRepository;
import com.perspectia.perspectiabackend.repositories.UserRepository;
import com.perspectia.perspectiabackend.requests.PerspectiveRequest;
import com.perspectia.perspectiabackend.responses.PerspectiveResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PerspectiveService {

    private PerspectiveRepository perspectiveRepository;

    private UserRepository userRepository;

    private TopicRepository topicRepository;

    private ChatModel chatModel;

    public PerspectiveService(PerspectiveRepository perspectiveRepository,  UserRepository userRepository, TopicRepository topicRepository, ChatModel chatModel) {
        this.perspectiveRepository = perspectiveRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.chatModel = chatModel;

    }

    public PerspectiveResponse createPerspective(PerspectiveRequest perspectiveRequest) {
        Perspective perspective = new Perspective();
        perspective.setUser(userRepository.findById(perspectiveRequest.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found")));
        perspective.setTopic(topicRepository.findById(perspectiveRequest.getTopicId()).orElseThrow(() -> new TopicNotFoundException("Topic not found")));
        perspective.setContent(perspectiveRequest.getContent());
        perspective.setCreatedAt(LocalDateTime.now());
        perspectiveRepository.save(perspective);
        return perspectiveToPerspective(perspective);

    }

    public PerspectiveResponse perspectiveToPerspective(Perspective perspective) {
        PerspectiveResponse perspectiveResponse = new PerspectiveResponse();
        perspectiveResponse.setContent(perspective.getContent());
        perspectiveResponse.setUserId(perspective.getUser().getId());
        perspectiveResponse.setTopicId(perspective.getTopic().getId());
        return perspectiveResponse;
    }

    public Page<PerspectiveResponse> getAllPerspectives(Pageable pageable, UUID topicId) {
        Page<PerspectiveResponse> perspectives = perspectiveRepository.findByTopicId(topicId, pageable);
        return perspectives;
    }

    public PerspectiveResponse getPerspectiveByUser(UUID userId, UUID topicId) {
        return perspectiveRepository.findByUserId(userId, topicId).orElseThrow(() -> new PerspectiveNotFoundException("Perspective Not Found"));
    }



}

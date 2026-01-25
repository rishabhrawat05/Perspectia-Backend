package com.perspectia.perspectiabackend.controllers;

import com.perspectia.perspectiabackend.responses.TopicResponse;
import com.perspectia.perspectiabackend.services.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perspectia")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/topic/random")
    public String randomTopic() {
        return topicService.generateRandomTopic();
    }

    @GetMapping("/topic/latest")
    public ResponseEntity<TopicResponse> latestTopic() {
        return ResponseEntity.ok(topicService.getLatestTopic());
    }

}

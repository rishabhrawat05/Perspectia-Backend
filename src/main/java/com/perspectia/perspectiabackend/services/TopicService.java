package com.perspectia.perspectiabackend.services;

import com.perspectia.perspectiabackend.enums.TopicStatus;
import com.perspectia.perspectiabackend.exceptions.TopicNotFoundException;
import com.perspectia.perspectiabackend.models.Topic;
import com.perspectia.perspectiabackend.repositories.TopicRepository;
import com.perspectia.perspectiabackend.responses.TopicResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TopicService {

    private final ChatModel chatModel;

    private final TopicRepository topicRepository;

    public TopicService(ChatModel chatModel, TopicRepository topicRepository) {
        this.chatModel = chatModel;
        this.topicRepository = topicRepository;
    }

    @Scheduled(
            cron = "0 0 9 * * ?",
            zone = "Asia/Kolkata"
    )
    public String generateRandomTopic() {
        String promptText = """
            Generate ONE random thought-provoking topic.
            The topic should encourage users to share their personal perspective or opinion.               
            Keep it short (one line).
            Do NOT add explanation, examples, or quotes.
        """;

        Prompt prompt = new Prompt(promptText);
        String result = chatModel.call(prompt).getResult().getOutput().getText();
        Topic topic = new Topic();
        ZoneId IST = ZoneId.of("Asia/Kolkata");
        LocalDate today = LocalDate.now(IST);
        topic.setTitle(result);
        topic.setDescription(result);
        topic.setCreatedAt(LocalDateTime.now());
        topic.setCreatedBy(chatModel.getDefaultOptions().getModel());
        topic.setStatus(TopicStatus.OPEN);
        topic.setTopicDate(today);
        topicRepository.save(topic);

        return result;

    }

    public TopicResponse getLatestTopic(){
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        TopicResponse topicResponse = new TopicResponse();

        int hour = now.getHour();

        if (hour < 9 || hour >= 21) {
            topicResponse.setContent("Topic Not Available");
            return topicResponse;
        }

        Topic topic = topicRepository.findByTopicDate(now.toLocalDate()).orElseThrow(() -> new TopicNotFoundException("Topic Not Found"));
        topicResponse.setId(topic.getId());
        topicResponse.setContent(topic.getTitle());
        return topicResponse;
    }
}

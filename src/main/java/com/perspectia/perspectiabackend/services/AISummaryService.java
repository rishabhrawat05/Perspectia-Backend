package com.perspectia.perspectiabackend.services;

import com.perspectia.perspectiabackend.exceptions.AISummaryNotFoundException;
import com.perspectia.perspectiabackend.models.AISummary;
import com.perspectia.perspectiabackend.models.Perspective;
import com.perspectia.perspectiabackend.models.Topic;
import com.perspectia.perspectiabackend.repositories.AISummaryRepository;
import com.perspectia.perspectiabackend.repositories.PerspectiveRepository;
import com.perspectia.perspectiabackend.repositories.TopicRepository;
import com.perspectia.perspectiabackend.responses.AISummaryResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AISummaryService {

    private AISummaryRepository summaryRepository;

    private PerspectiveRepository perspectiveRepository;

    private TopicRepository topicRepository;

    private ChatModel chatModel;

    public AISummaryService(AISummaryRepository summaryRepository, PerspectiveRepository perspectiveRepository, TopicRepository topicRepository, ChatModel chatModel) {
        this.summaryRepository = summaryRepository;
        this.perspectiveRepository = perspectiveRepository;
        this.topicRepository = topicRepository;
        this.chatModel = chatModel;
    }

    public String buildPrompt(String topicTitle, List<Perspective> perspectives) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
    You are an unbiased analyst.

    Topic:
    "%s"

    Given the following user perspectives on the above topic:
    - Identify common viewpoints
    - Highlight opposing opinions
    - Extract key insights
    - Produce a neutral summary (120–150 words)

    Do not mention usernames.
    Do not add new opinions.
    Do not take sides.
    
    Tone: clear, thoughtful, balanced.

    Perspectives:
    """.formatted(topicTitle));

        int i = 1;
        for (Perspective p : perspectives) {
            sb.append(i++).append(". ")
                    .append(p.getContent())
                    .append("\n");
        }

        return sb.toString();
    }


    @Scheduled(cron = "0 0 21 * * ?", zone = "Asia/Kolkata")
    public void generateSummary() {

        ZoneId IST = ZoneId.of("Asia/Kolkata");

        Optional<Topic> topicOpt =
                topicRepository.findByTopicDate(LocalDate.now(IST));

        if (topicOpt.isEmpty()) return;

        Topic topic = topicOpt.get();

        if (summaryRepository.findByTopicId(topic.getId()).isEmpty()) return;

        List<Perspective> validPerspectives =
                perspectiveRepository.findByTopicId(topic.getId())
                        .stream()
                        .filter(p -> p.getContent().length() > 50)
                        .toList();

        if (validPerspectives.size() < 3) return;

        String prompt = buildPrompt(topic.getTitle(), validPerspectives);

        String summary;
        try {
            summary = summarize(prompt);
        } catch (Exception e) {
            return;
        }

        if (summary == null || summary.length() < 100) return;

        AISummary aiSummary = new AISummary();
        aiSummary.setTopic(topic);
        aiSummary.setSummaryText(summary);
        aiSummary.setModelUsed(chatModel.getDefaultOptions().getModel());
        aiSummary.setGeneratedAt(LocalDateTime.now());
        aiSummary.setDiscussionDate(getCurrentDiscussionDate());
        summaryRepository.save(aiSummary);
    }


    public String summarize(String prompt) {

        ChatResponse response = chatModel.call(
                new Prompt(prompt)
        );

        return response.getResult()
                .getOutput()
                .getText();
    }

    public AISummaryResponse getLatestAISummary(){
        AISummary aiSummary = summaryRepository.findByDiscussionDate(getCurrentDiscussionDate()).orElseThrow(() -> new AISummaryNotFoundException("AI Summary Not Found"));
        AISummaryResponse response = new AISummaryResponse();
        response.setSummaryText(aiSummary.getSummaryText());
        response.setModelUsed(aiSummary.getModelUsed());
        response.setTopic(aiSummary.getTopic().getTitle());
        response.setGeneratedAt(aiSummary.getGeneratedAt());
        return response;
    }

    public static LocalDate getCurrentDiscussionDate() {
        ZoneId IST = ZoneId.of("Asia/Kolkata");
        ZonedDateTime now = ZonedDateTime.now(IST);

        if (now.getHour() < 9) {
            return now.toLocalDate().minusDays(1);
        }
        return now.toLocalDate();
    }

}

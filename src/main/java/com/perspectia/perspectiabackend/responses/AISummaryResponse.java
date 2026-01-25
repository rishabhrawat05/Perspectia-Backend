package com.perspectia.perspectiabackend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AISummaryResponse {

    private UUID id;

    private String topic;

    private String summaryText;

    private String modelUsed;

    private LocalDateTime generatedAt;
}

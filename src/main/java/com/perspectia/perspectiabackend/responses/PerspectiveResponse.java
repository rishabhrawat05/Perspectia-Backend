package com.perspectia.perspectiabackend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerspectiveResponse {

    private UUID topicId;

    private UUID userId;

    private String content;
}

package com.perspectia.perspectiabackend.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerspectiveRequest {

    private UUID topicId;

    private UUID userId;

    private String content;

}

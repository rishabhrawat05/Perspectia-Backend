package com.perspectia.perspectiabackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_summaries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AISummary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    private Topic topic;

    @Column(columnDefinition = "TEXT")
    private String summaryText;

    private String modelUsed;

    private LocalDateTime generatedAt;

    private LocalDate discussionDate;
}

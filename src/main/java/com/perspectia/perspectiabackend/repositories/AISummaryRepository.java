package com.perspectia.perspectiabackend.repositories;

import com.perspectia.perspectiabackend.models.AISummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AISummaryRepository extends JpaRepository<AISummary, UUID> {

    @Query("""
    SELECT ai FROM AISummary ai WHERE ai.topic.id = :topicId
""")
    Optional<AISummary> findByTopicId(UUID topicId);

    Optional<AISummary> findByDiscussionDate(LocalDate discussionDate);
}

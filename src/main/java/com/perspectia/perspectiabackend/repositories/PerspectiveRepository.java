package com.perspectia.perspectiabackend.repositories;

import com.perspectia.perspectiabackend.models.Perspective;
import com.perspectia.perspectiabackend.responses.PerspectiveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PerspectiveRepository extends JpaRepository<Perspective, UUID> {

    @Query("""
    SELECT new com.perspectia.perspectiabackend.responses.PerspectiveResponse(
    p.topic.id,
    p.user.id,
    p.content
    )
    FROM Perspective p
    WHERE p.topic.id = :topicId
    AND p.deletedAt IS NULL
    """)
    Page<PerspectiveResponse> findByTopicId(UUID topicId, Pageable pageable);

    List<Perspective> findByTopicId(UUID topicId);
}

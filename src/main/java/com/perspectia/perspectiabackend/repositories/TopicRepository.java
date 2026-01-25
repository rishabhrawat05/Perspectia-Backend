package com.perspectia.perspectiabackend.repositories;

import com.perspectia.perspectiabackend.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {

    Optional<Topic> findByTopicDate(LocalDate date);

}

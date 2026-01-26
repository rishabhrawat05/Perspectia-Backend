package com.perspectia.perspectiabackend.controllers;


import com.perspectia.perspectiabackend.requests.PerspectiveRequest;
import com.perspectia.perspectiabackend.responses.PerspectiveResponse;
import com.perspectia.perspectiabackend.services.PerspectiveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/perspectia/perspective")
public class PerspectiveController {

    private PerspectiveService perspectiveService;

    public PerspectiveController(PerspectiveService perspectiveService) {
        this.perspectiveService = perspectiveService;
    }

    @PostMapping("/create")
    public ResponseEntity<PerspectiveResponse> createPerspective(@RequestBody PerspectiveRequest perspectiveRequest) {
        return ResponseEntity.ok(perspectiveService.createPerspective(perspectiveRequest));
    }

    @GetMapping("/get-all")
    public Page<PerspectiveResponse> getAllPerspectives(@RequestParam int page, @RequestParam int size, @RequestParam UUID topicId) {
        Pageable pageable = PageRequest.of(page, size);
        return perspectiveService.getAllPerspectives(pageable, topicId);
    }

    @GetMapping("/get-by-user")
    public ResponseEntity<PerspectiveResponse> getPerspectiveByUser(@RequestParam UUID userId, @RequestParam UUID topicId) {
        return ResponseEntity.ok(perspectiveService.getPerspectiveByUser(userId, topicId));
    }
}

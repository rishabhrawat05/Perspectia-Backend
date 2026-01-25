package com.perspectia.perspectiabackend.controllers;

import com.perspectia.perspectiabackend.responses.AISummaryResponse;
import com.perspectia.perspectiabackend.services.AISummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perspectia/summary")
public class AISummaryController {

    private AISummaryService aiSummaryService;

    public AISummaryController(AISummaryService aiSummaryService) {
        this.aiSummaryService = aiSummaryService;
    }

    @GetMapping("/latest")
    public ResponseEntity<AISummaryResponse> getAISummaryResponse() {
        return ResponseEntity.ok(aiSummaryService.getLatestAISummary());
    }
}

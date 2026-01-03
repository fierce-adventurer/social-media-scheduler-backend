package com.fierceadventurer.analyticsservice.controllers;

import com.fierceadventurer.analyticsservice.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics/rag")
@RequiredArgsConstructor
@Slf4j
public class RAGController {

    public final EmbeddingService embeddingService;

    @GetMapping("/context/{socialAccountId")
    public ResponseEntity<List<String>> retrieveContext(
        @PathVariable UUID socialAccountId,
                @RequestParam String query){

        log.info("RAG Request : Retrieving context for account {} on topic '{}'", socialAccountId , query);

        List<String> context = embeddingService.findSimilarContent(socialAccountId, query);

        return ResponseEntity.ok(context);
    }

}

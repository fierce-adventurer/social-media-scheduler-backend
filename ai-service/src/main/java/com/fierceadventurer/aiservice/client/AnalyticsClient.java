package com.fierceadventurer.aiservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "analytics-service" , url = "${clients.analytics-service.url}")
public interface AnalyticsClient {

    @GetMapping("/api/v1/analytics/rag/context/{socialAccountId")
    List<String> getRelevantContext(
        @PathVariable ("socialAccountId")UUID socialAccountId,
                @RequestParam ("query") String query);
}

package com.fierceadventurer.analyticsservice.service;

import com.fierceadventurer.analyticsservice.dto.HistoricalPost;

import java.util.List;
import java.util.UUID;

public interface EmbeddingService {
    void ingestPosts(UUID socialAccountId , List<HistoricalPost> posts);
}

package com.fierceadventurer.analyticsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierceadventurer.analyticsservice.dto.HistoricalPost;
import com.fierceadventurer.analyticsservice.entity.UserPostEmbedding;
import com.fierceadventurer.analyticsservice.repository.UserPostEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService{
    private final EmbeddingModel embeddingModel;
    private final UserPostEmbeddingRepository embeddingRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Async
    @Transactional
    public void ingestPosts(UUID socialAccountId, List<HistoricalPost> posts) {
        log.info("Starting vector ingestion for account: {}", socialAccountId);

        if(posts == null || posts.isEmpty()){
            log.warn("No posts to ingest for account{}" , socialAccountId);
            return;
        }

        List<UserPostEmbedding> existingEntities = embeddingRepository.findBySocialAccountId(socialAccountId);
        Map<String , UserPostEmbedding> existingMap = new HashMap<>();
        for(UserPostEmbedding entity : existingEntities){
            try {
                Map<String, Object> meta = objectMapper.readValue(entity.getMetadata(), new TypeReference<>() {
                });
                if(meta.containsKey("posted_at")){
                    existingMap.put(String.valueOf(meta.get("posted_at")), entity);
                }
            }
            catch (Exception e){
                log.warn("Skipping malformed metadata for embedding ID: {}", entity.getId());
            }
        }

        List<UserPostEmbedding> toSave = new ArrayList<>();

        for(HistoricalPost post : posts){
            String keyDate = post.getCreatedAt().toString();

            String textContent = "Post created on " + post.getCreatedAt()
                    + " which recieved an engagement score of " + post.getEngagementCount();

            if(existingMap.containsKey(keyDate)){
                UserPostEmbedding existing = existingMap.get(keyDate);

                if(!existing.getContent().equals(textContent)){
                    log.debug("Updating embedding for post date: {}", keyDate);
                    existing.setContent(textContent);
                    existing.setEmbedding(generateVector(textContent));
                    existing.setMetadata(generateMetadataJson(post));
                    toSave.add(existing);
                }
            }
            else {
                log.debug("Creating new embedding for post date : {}" , keyDate);
                UserPostEmbedding newEntity = UserPostEmbedding.builder()
                        .socialAccountId(socialAccountId)
                        .content(textContent)
                        .embedding(generateVector(textContent))
                        .metadata(generateMetadataJson(post))
                        .build();
                toSave.add(newEntity);
            }
        }

        if(!toSave.isEmpty()){
            embeddingRepository.saveAll(toSave);
            log.info("Upsert complete. Saved/Updated {} vectors.", toSave.size());
        }
        else {
            log.info("No changes detected in vectors. ");
        }
    }

    private String generateMetadataJson(HistoricalPost post) {
        try{
            return objectMapper.writeValueAsString(Map.of(
                    "engagement_score", post.getEngagementCount(),
                    "posted_at", post.getCreatedAt().toString()
            ));
        }
        catch (JsonProcessingException e){
            log.error("Metadata serialization failed", e);
            return "{}";
        }
    }

    private double[] generateVector(String textContent) {
        List<Double> vectorList = embeddingModel.embed(textContent);
        return vectorList.stream().mapToDouble(Double::doubleValue).toArray();
    }
}

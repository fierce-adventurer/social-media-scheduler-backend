package com.fierceadventurer.analyticsservice.repository;

import com.fierceadventurer.analyticsservice.entity.UserPostEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPostEmbeddingRepository extends JpaRepository<UserPostEmbedding , UUID> {
    List<UserPostEmbedding> findBySocialAccountId(UUID socialAccountId);

    @Query(value = """
            SELECT * FROM user_post_embeddings
            WHERE social_account_id = :socialAccountId
            ORDER BY embedding <=> :queryVector
            LIMIT 5
            """, nativeQuery = true)
    List<UserPostEmbedding> findMostSimilar(UUID socialAccountId, double[] queryVector);
}

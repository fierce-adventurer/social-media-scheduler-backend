package com.fierceadventurer.analyticsservice.repository;

import com.fierceadventurer.analyticsservice.entity.UserPostEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPostEmbeddingRepository extends JpaRepository<UserPostEmbedding , UUID> {
    List<UserPostEmbedding> findBySocialAccountId(UUID socialAccountId);
}

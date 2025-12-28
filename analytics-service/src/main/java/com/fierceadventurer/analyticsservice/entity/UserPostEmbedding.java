package com.fierceadventurer.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "user_post_embeddings")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserPostEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "social_ccount_id", nullable = false)
    private UUID socialAccountId;

    @Column(nullable = false , columnDefinition = "TEXT")
    private String content;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "embedding" , columnDefinition = "vector(1536")
    @JdbcTypeCode(SqlTypes.VECTOR)
    private double[] embedding;
}

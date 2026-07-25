package com.tunematch.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "songs")
public class Song {

    @Id
    @Column(name = "track_id", length = 30)
    private String trackId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String artist;

    @Column(nullable = false, length = 50)
    private String genre;

    @Column(name = "duration_ms")
    private Integer durationMs;

    private Integer popularity;

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "audio_features", columnDefinition = "json")
    private Map<String, Object> audioFeatures;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

package com.tunematch.backend.dto;

import com.tunematch.backend.model.Song;

public record SongSummaryDTO(
        String trackId,
        String title,
        String artist,
        String genre,
        Integer durationMs,
        Integer popularity
) {
    public static SongSummaryDTO fromEntity(Song song) {
        return new SongSummaryDTO(
                song.getTrackId(),
                song.getTitle(),
                song.getArtist(),
                song.getGenre(),
                song.getDurationMs(),
                song.getPopularity()
        );
    }
}
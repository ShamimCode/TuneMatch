package com.tunematch.backend.dto;

import com.tunematch.backend.model.Playlist;

import java.time.LocalDateTime;

public record PlaylistResponseDTO(
        Long playlistId,
        String name,
        LocalDateTime createdAt
) {
    public static PlaylistResponseDTO fromEntity(Playlist playlist) {
        return new PlaylistResponseDTO(
                playlist.getPlaylistId(),
                playlist.getName(),
                playlist.getCreatedAt()
        );
    }
}
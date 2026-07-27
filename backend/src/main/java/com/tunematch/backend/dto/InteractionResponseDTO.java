package com.tunematch.backend.dto;

import com.tunematch.backend.model.UserInteraction;

import java.time.LocalDateTime;

public record InteractionResponseDTO(
        String trackId,
        String title,
        String artist,
        String interactionType,
        LocalDateTime createdAt
) {
    public static InteractionResponseDTO fromEntity(UserInteraction interaction) {
        return new InteractionResponseDTO(
                interaction.getSong().getTrackId(),
                interaction.getSong().getTitle(),
                interaction.getSong().getArtist(),
                interaction.getInteractionType().toString(),
                interaction.getCreatedAt()
        );
    }
}
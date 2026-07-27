package com.tunematch.backend.controller;

import com.tunematch.backend.dto.InteractionResponseDTO;
import com.tunematch.backend.dto.SongSummaryDTO;
import com.tunematch.backend.model.*;
import com.tunematch.backend.repository.*;
import com.tunematch.backend.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interactions")
public class UserInteractionController {

    @Autowired
    private UserInteractionRepository interactionRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"));
    }

    @PostMapping("/{trackId}")
    public ResponseEntity<?> recordInteraction(
            @PathVariable String trackId,
            @RequestBody Map<String, String> body
    ) {
        User user = getCurrentUser();

        Song song = songRepository.findById(trackId).orElse(null);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }

        String typeStr = body.get("type");
        InteractionType type;
        try {
            type = InteractionType.valueOf(typeStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid interaction type. Use: like, play, skip"));
        }

        UserInteraction interaction = new UserInteraction();
        interaction.setUser(user);
        interaction.setSong(song);
        interaction.setInteractionType(type);
        interaction.setCreatedAt(LocalDateTime.now());

        interactionRepository.save(interaction);
        return ResponseEntity.ok(Map.of("message", "Interaction recorded"));
    }

    @GetMapping
    public ResponseEntity<List<InteractionResponseDTO>> getMyInteractions() {
        User user = getCurrentUser();

        List<InteractionResponseDTO> results = interactionRepository.findByUser_UserId(user.getUserId())
                .stream()
                .map(InteractionResponseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(results);
    }

    @GetMapping("/likes")
    public ResponseEntity<List<SongSummaryDTO>> getMyLikes() {
        User user = getCurrentUser();

        List<SongSummaryDTO> results = interactionRepository
                .findByUser_UserIdAndInteractionType(user.getUserId(), InteractionType.like)
                .stream()
                .map(i -> SongSummaryDTO.fromEntity(i.getSong()))
                .toList();

        return ResponseEntity.ok(results);
    }
}
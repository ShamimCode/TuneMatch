package com.tunematch.backend.controller;


import com.tunematch.backend.dto.PlaylistResponseDTO;
import com.tunematch.backend.model.Playlist;
import com.tunematch.backend.model.User;
import com.tunematch.backend.repository.PlaylistRepository;
import com.tunematch.backend.repository.UserRepository;
import com.tunematch.backend.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponseDTO>> getMyPlaylists() {
        User user = getCurrentUser();
        List<PlaylistResponseDTO> playlists = playlistRepository.findByUser_UserId(user.getUserId())
                .stream()
                .map(PlaylistResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(playlists);
    }

    @PostMapping
    public ResponseEntity<PlaylistResponseDTO> createPlaylist(@RequestBody Map<String, String> body) {
        User user = getCurrentUser();

        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName(body.get("name"));
        playlist.setCreatedAt(LocalDateTime.now());

        Playlist saved = playlistRepository.save(playlist);
        return ResponseEntity.ok(PlaylistResponseDTO.fromEntity(saved));
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long playlistId) {
        User user = getCurrentUser();

        return playlistRepository.findById(playlistId)
                .map(playlist -> {
                    if (!playlist.getUser().getUserId().equals(user.getUserId())) {
                        return ResponseEntity.status(403).body(Map.of("error", "Not your playlist"));
                    }
                    playlistRepository.delete(playlist);
                    return ResponseEntity.ok(Map.of("message", "Playlist deleted"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

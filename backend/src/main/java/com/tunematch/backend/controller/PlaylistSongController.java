package com.tunematch.backend.controller;

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
@RequestMapping("/api/playlists/{playlistId}/songs")
public class PlaylistSongController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"));
    }

    private Playlist getOwnedPlaylistOrThrow(Long playlistId) {
        User user = getCurrentUser();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("Not your playlist");
        }
        return playlist;
    }

    @GetMapping
    public ResponseEntity<?> getSongsInPlaylist(@PathVariable Long playlistId) {
        try {
            getOwnedPlaylistOrThrow(playlistId);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        List<SongSummaryDTO> songs = playlistSongRepository.findByPlaylist_PlaylistId(playlistId)
                .stream()
                .map(ps -> SongSummaryDTO.fromEntity(ps.getSong()))
                .toList();

        return ResponseEntity.ok(songs);
    }

    @PostMapping("/{trackId}")
    public ResponseEntity<?> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable String trackId) {
        Playlist playlist;
        try {
            playlist = getOwnedPlaylistOrThrow(playlistId);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        Song song = songRepository.findById(trackId)
                .orElse(null);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }

        PlaylistSongId id = new PlaylistSongId(playlistId, trackId);
        if (playlistSongRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Song already in playlist"));
        }

        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setId(id);
        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);
        playlistSong.setAddedAt(LocalDateTime.now());

        playlistSongRepository.save(playlistSong);
        return ResponseEntity.ok(Map.of("message", "Song added to playlist"));
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<?> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable String trackId) {
        try {
            getOwnedPlaylistOrThrow(playlistId);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        PlaylistSongId id = new PlaylistSongId(playlistId, trackId);
        if (!playlistSongRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        playlistSongRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Song removed from playlist"));
    }
}
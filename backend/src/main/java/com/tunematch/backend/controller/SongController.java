package com.tunematch.backend.controller;


import com.tunematch.backend.dto.SongSummaryDTO;
import com.tunematch.backend.model.Song;
import com.tunematch.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping
    public ResponseEntity<List<SongSummaryDTO>> getAllSongs() {
        List<SongSummaryDTO> songs = songRepository.findAll().stream()
                .map(SongSummaryDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<Song> getSongById(@PathVariable String trackId) {
        return songRepository.findById(trackId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<SongSummaryDTO>> getSongsByGenre(@PathVariable String genre) {
        List<SongSummaryDTO> songs = songRepository.findByGenre(genre).stream()
                .map(SongSummaryDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SongSummaryDTO>> searchSongs(@RequestParam String query) {
        List<Song> byTitle = songRepository.findByTitleContainingIgnoreCase(query);
        List<Song> byArtist = songRepository.findByArtistContainingIgnoreCase(query);

        byTitle.addAll(byArtist.stream()
                .filter(s -> !byTitle.contains(s))
                .toList());

        List<SongSummaryDTO> results = byTitle.stream()
                .map(SongSummaryDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(results);
    }
}

package com.tunematch.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class PlaylistSong {

    @EmbeddedId
    private PlaylistSongId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trackId")
    @JoinColumn(name = "track_id")
    private Song song;

    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;
}

package com.tunematch.backend.repository;

import com.tunematch.backend.model.PlaylistSong;
import com.tunematch.backend.model.PlaylistSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {

    List<PlaylistSong> findByPlaylist_PlaylistId(Long playlistId);
}
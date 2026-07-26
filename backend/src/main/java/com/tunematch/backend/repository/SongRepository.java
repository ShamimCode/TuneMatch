package com.tunematch.backend.repository;

import com.tunematch.backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByGenre(String genre);

    List<Song> findByArtistContainingIgnoreCase(String artist);

    List<Song> findByTitleContainingIgnoreCase(String title);
}

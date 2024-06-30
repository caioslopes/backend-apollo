package br.com.apollomusic.app.repository;

import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.model.entities.Song;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {

    @Modifying
    default void addSongsToPlaylist(String playlistId, Set<Song> songs){
        Playlist playlist = findById(playlistId).orElseThrow();
        for(Song song : songs ){
            playlist.addSong(song);
        }
        save(playlist);
    }

    @Modifying
    default void removeSongsFromPlaylist(String playlistId, Set<Song> songs){
        Playlist playlist = findById(playlistId).orElseThrow();
        for(Song song : songs ){
            playlist.removeSong(song);
        }
        save(playlist);
    }

    @Modifying
    default void setLastSnapshotId(String playlistId, String lastSnapshotId){
        Playlist playlist = findById(playlistId).orElseThrow();
        playlist.setLastSnapshotId(lastSnapshotId);
        save(playlist);
    }

    @Modifying
    default void blockGenres(String playlistId, Set<String> genres){
        Playlist playlist = findById(playlistId).orElseThrow();
        playlist.setBlockedGenres(genres);
        save(playlist);
    }

    @Modifying
    default void incrementVoteGenres(String playlistId, Set<String> genres){
        Playlist playlist = findById(playlistId).orElseThrow();
        playlist.incrementVoteGenre(genres);
        save(playlist);
    }

    @Modifying
    default void decrementVoteGenres(String playlistId, Set<String> genres){
        Playlist playlist = findById(playlistId).orElseThrow();
        playlist.decrementVoteGenre(genres);
        save(playlist);
    }

}

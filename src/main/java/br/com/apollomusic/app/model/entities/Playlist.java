package br.com.apollomusic.app.model.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playlist_id")
    private long playlistId;

    @OneToOne(mappedBy = "playlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Establishment establishment;

    @ElementCollection
    @CollectionTable(name = "tb_playlist_songs", joinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Song> songs = new HashSet<>();

    public Playlist(long playlistId, Establishment establishment, Set<Song> songs) {
        this.playlistId = playlistId;
        this.establishment = establishment;
        this.songs = songs;
    }

    public Playlist() {}

    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
}

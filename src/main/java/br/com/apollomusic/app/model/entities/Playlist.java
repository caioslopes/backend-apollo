package br.com.apollomusic.app.model.entities;


import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    @ElementCollection
    @CollectionTable(name = "tb_blocked_genres_playlist", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "genre")
    private Set<String> blockedGenres = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "tb_genres_playlist", joinColumns = @JoinColumn(name = "playlist_id"))
    @MapKeyColumn(name = "genre")
    @Column(name = "votes")
    private Map<String, Integer> genres = new HashMap<>();

    public Playlist(long playlistId, Establishment establishment, Set<Song> songs, Set<String> blockedGenres, Map<String, Integer> genres) {
        this.playlistId = playlistId;
        this.establishment = establishment;
        this.songs = songs;
        this.blockedGenres = blockedGenres;
        this.genres = genres;
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

    public Set<String> getBlockedGenres() {
        return blockedGenres;
    }

    public void setBlockedGenres(Set<String> blockedGenres) {
        this.blockedGenres = blockedGenres;
    }

    public Map<String, Integer> getGenres() {
        return genres;
    }

    public void setGenres(Map<String, Integer> genres) {
        this.genres = genres;
    }

    public Integer getVotesQuantity(){
        Integer votesQuantity = 0;
       for(Map.Entry<String, Integer> entry: genres.entrySet()){
           if(entry.getValue() != 0){
               votesQuantity++;
           }
       }
       return votesQuantity;
    }

}

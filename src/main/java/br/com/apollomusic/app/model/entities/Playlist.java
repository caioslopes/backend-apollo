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
    @Column(name = "playlist_id")
    private String playlistId;

    private String lastSnapshot_id;

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

    public Playlist(String playlistId, Establishment establishment, Set<Song> songs, Set<String> blockedGenres, Map<String, Integer> genres, String lastSnapshot_id) {
        this.playlistId = playlistId;
        this.establishment = establishment;
        this.songs = songs;
        this.blockedGenres = blockedGenres;
        this.genres = genres;
        this.lastSnapshot_id = lastSnapshot_id;
    }

    public Playlist() {}

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
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

    public String getLastSnapshot_id() {
        return lastSnapshot_id;
    }

    public void setLastSnapshot_id(String lastSnapshot_id) {
        this.lastSnapshot_id = lastSnapshot_id;
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

    public void addSong(Song song){
        this.songs.add(song);
    }

    public void removeSong(Song song){
        songs.removeIf(s -> s.getUri().equals(song.getUri()));
    }

}

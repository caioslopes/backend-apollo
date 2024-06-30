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

    private String lastSnapshotId;

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

    public Playlist(String playlistId, Establishment establishment, Set<Song> songs, Set<String> blockedGenres, Map<String, Integer> genres, String lastSnapshotId) {
        this.playlistId = playlistId;
        this.establishment = establishment;
        this.songs = songs;
        this.blockedGenres = blockedGenres;
        this.genres = genres;
        this.lastSnapshotId = lastSnapshotId;
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

    public String getLastSnapshotId() {
        return lastSnapshotId;
    }

    public void setLastSnapshotId(String lastSnapshotId) {
        this.lastSnapshotId = lastSnapshotId;
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

    public void incrementVoteGenre(Set<String> genres){
        for(String genre: genres){
            if(!this.blockedGenres.contains(genre)){
                if(this.genres.containsKey(genre)){
                    this.genres.put(genre, this.genres.get(genre) + 1);
                }else{
                    this.genres.put(genre, 1);
                }
            }
        }
    }

    public void decrementVoteGenre(Set<String> genres){
        for(String genre: genres){
            if(this.genres.containsKey(genre)){
                if(this.genres.get(genre) > 0){
                    this.genres.put(genre, this.genres.get(genre) - 1);
                }else{
                    this.genres.remove(genre);
                }

            }
        }
    }

}

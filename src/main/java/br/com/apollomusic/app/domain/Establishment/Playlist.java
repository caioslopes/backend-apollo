package br.com.apollomusic.app.domain.Establishment;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Playlist {

    public static final int SONGLIMIT = 50;

    @Id
    private String id;

    private String snapshot;

    @ElementCollection
    @CollectionTable(name = "blocked_genres_playlist", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "genre")
    private Collection<String> blockedGenres = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "genres_playlist", joinColumns = @JoinColumn(name = "playlist_id"))
    @MapKeyColumn(name = "genre")
    @Column(name = "votes")
    private Map<String, Integer> genres = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "playlist_songs", joinColumns = @JoinColumn(name = "playlist_id"))
    private Collection<Song> songs = new HashSet<>();

    public Playlist() {
    }

    public Playlist(String id, String snapshot, Collection<String> blockedGenres, Map<String, Integer> genres, Collection<Song> songs) {
        this.id = id;
        this.snapshot = snapshot;
        this.blockedGenres = blockedGenres;
        this.genres = genres;
        this.songs = songs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public Collection<String> getBlockedGenres() {
        return blockedGenres;
    }

    public void setBlockedGenres(Collection<String> blockedGenres) {
        this.blockedGenres = blockedGenres;
    }

    public Map<String, Integer> getGenres() {
        return genres;
    }

    public void setGenres(Map<String, Integer> genres) {
        this.genres = genres;
    }

    public Collection<Song> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
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

    public void addBlockGenres(Set<String> blockedGenres){
        this.blockedGenres.addAll(blockedGenres);
        removeGenres(blockedGenres);
    }

    public void removeBlockGenres(Set<String> blockedGenres){
        this.blockedGenres.removeAll(blockedGenres);
        addGenres(blockedGenres);
    }

    public void incrementVoteGenre(Set<String> genres){
        for(String genre: genres){
            if(this.genres.containsKey(genre)){
                this.genres.put(genre, this.genres.get(genre) + 1);
            }
        }
    }

    public void decrementVoteGenre(Set<String> genres){
        for(String genre: genres){
            if(this.genres.containsKey(genre)){
                if(this.genres.get(genre) > 0){
                    this.genres.put(genre, this.genres.get(genre) - 1);
                }
            }
        }
    }

    private void removeGenres(Set<String> genres){
        for(String genre : genres){
            this.genres.remove(genre);
        }
    }

    private void addGenres(Set<String> genres){
        for(String genre : genres){
            this.genres.put(genre, 1);
        }
    }
}
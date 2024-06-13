package br.com.apollomusic.app.repository.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Song {
    private String songId;
    private String genre;

    public Song(String songId, String genre) {
        this.songId = songId;
        this.genre = genre;
    }

    public Song() {

    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

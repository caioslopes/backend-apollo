package br.com.apollomusic.app.model.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Song {
    private String uri;
    private String genre;

    public Song(String uri, String genre) {
        this.uri = uri;
        this.genre = genre;
    }

    public Song() {}

    public String getUri() {
        return uri;
    }

    public void setUri(String songId) {
        this.uri = songId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

package br.com.apollomusic.app.domain.Establishment;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Immutable;

@Immutable
@Embeddable
public class Song {

    private String uri;
    private String genre;

    public Song() {
    }

    public Song(String uri, String genre) {
        this.uri = uri;
        this.genre = genre;
    }

    public String getUri() {
        return uri;
    }

    public String getGenre() {
        return genre;
    }
}
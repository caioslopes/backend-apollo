package br.com.apollomusic.app.domain.Establishment;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Immutable;

@Immutable
@Embeddable
public class Song {

    private String id;
    private String genre;

    public Song() {
    }

    public Song(String id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getUri(){
        return "spotify:track:" + id;
    }
}
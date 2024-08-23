package br.com.apollomusic.app.domain.Establishment;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.util.Collection;

@Immutable
@Embeddable
public class User {

    private String genres;
    private Long expiresIn;

    public User() {}

    public User(String genres) {
        this.genres = genres;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void addGenre(Collection<String> genres) {
        this.genres = String.join(",", genres);
    }
}

package br.com.apollomusic.app.domain.Establishment;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class User {

    @Id
    private Long id;

    private String username;

    @ElementCollection
    @CollectionTable(name = "user_genres", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "genre")
    private Collection<String> genres;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id")
    private Establishment establishment;

    public User() {
    }

    public User(Long id, String username, Collection<String> genres) {
        this.id = id;
        this.username = username;
        this.genres = genres;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Collection<String> getGenres() {
        return genres;
    }
}
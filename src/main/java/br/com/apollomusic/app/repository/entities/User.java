package br.com.apollomusic.app.repository.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id")
    private Establishment establishment;

    @ElementCollection
    @CollectionTable(name = "tb_user_genres", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    public User() {}

    public User(Long userId, Establishment establishment, Set<String> genres, String userName) {
        this.userId = userId;
        this.establishment = establishment;
        this.genres = genres;
        this.userName = userName;
    }

    public User(Establishment establishment, String userName, Set<String> genres) {
        this.establishment = establishment;
        this.userName = userName;
        this.genres = genres;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

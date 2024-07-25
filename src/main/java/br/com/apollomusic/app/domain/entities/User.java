package br.com.apollomusic.app.domain.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Embeddable
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id")
    private Establishment establishment;

    @ElementCollection
    @CollectionTable(name = "tb_user_genres", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(Long userId, String username, Establishment establishment, Set<String> genres, Set<Role> roles) {
        this.userId = userId;
        this.username = username;
        this.establishment = establishment;
        this.genres = genres;
        this.roles = roles;
    }

    public User(Establishment establishment, String username, Set<String> genres, Set<Role> roles) {
        this.establishment = establishment;
        this.username = username;
        this.genres = genres;
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

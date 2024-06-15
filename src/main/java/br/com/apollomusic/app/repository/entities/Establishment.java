package br.com.apollomusic.app.repository.entities;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "tb_establishment")
public class Establishment {

    @Id
    @Column(name = "establishment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long establishmentId;
    private String deviceId;
    private boolean isOff;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "tb_blocked_genres_establishment", joinColumns = @JoinColumn(name = "establishment_id"))
    @Column(name = "genre")
    private Set<String> blockedGenres = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "tb_genres_establishment", joinColumns = @JoinColumn(name = "establishment_id"))
    @MapKeyColumn(name = "genre")
    @Column(name = "votes")
    private Map<String, Integer> genres = new HashMap<>();


    public Establishment(Long establishmentId, String deviceId, boolean isOff, Owner owner, Playlist playlist, Set<User> users, Set<String> blockedGenres, Map<String, Integer> genres) {
        this.establishmentId = establishmentId;
        this.deviceId = deviceId;
        this.isOff = isOff;
        this.owner = owner;
        this.playlist = playlist;
        this.users = users;
        this.blockedGenres = blockedGenres;
        this.genres = genres;
    }

    public Establishment() {}

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isOff() {
        return isOff;
    }

    public void setOff(boolean off) {
        isOff = off;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
}

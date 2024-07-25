package br.com.apollomusic.app.domain.entities;

import jakarta.persistence.*;

import java.util.HashSet;
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

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ElementCollection
    @CollectionTable(name = "tb_establishment_playlist", joinColumns = @JoinColumn(name = "establishment_id"))
    private Playlist playlist;

    @ElementCollection
    @CollectionTable(name = "tb_playlist_songs", joinColumns = @JoinColumn(name = "establishment_id"))
    private Set<User> users = new HashSet<>();


    public Establishment(Long establishmentId, String name, String deviceId, boolean isOff, Owner owner, Playlist playlist, Set<User> users) {
        this.establishmentId = establishmentId;
        this.name = name;
        this.deviceId = deviceId;
        this.isOff = isOff;
        this.owner = owner;
        this.playlist = playlist;
        this.users = users;
    }

    public Establishment() {}

    public Long getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Long establishmentId) {
        this.establishmentId = establishmentId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

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
}

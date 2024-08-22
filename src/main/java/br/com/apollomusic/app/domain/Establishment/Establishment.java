package br.com.apollomusic.app.domain.Establishment;

import br.com.apollomusic.app.domain.Owner.Owner;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String deviceId;

    private boolean isOff;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToOne(mappedBy = "establishment", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private Playlist playlist;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user", joinColumns = @JoinColumn(name = "establishment_id"))
    private Collection<User> users = new HashSet<>();

    public Establishment() {
    }

    public Establishment(Long id, String deviceId, boolean isOff, String name, Owner owner, Playlist playlist, Collection<User> users) {
        this.id = id;
        this.deviceId = deviceId;
        this.isOff = isOff;
        this.name = name;
        this.owner = owner;
        this.playlist = playlist;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Collection<User> getUser() {
        return users;
    }

    public void setUser(Collection<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        user.setExpiresIn(System.currentTimeMillis() + 5 * 60 * 1000);
        users.add(user);
    }
}

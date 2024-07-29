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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<User> users = new HashSet<>();

    public Establishment() {
    }

    public Establishment(Long id, String deviceId, boolean isOff, String name, Collection<User> users) {
        this.id = id;
        this.deviceId = deviceId;
        this.isOff = isOff;
        this.name = name;
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

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public User getUser(Long id){
        //TO-DO
        return null;
    }

    public User addUser(User user){
        //TO-DO
        return null;
    }

    public User removeUser(Long id){
        //TO-DO
        return null;
    }

}

package br.com.apollomusic.app.domain.Establishment;

import br.com.apollomusic.app.domain.Owner.Owner;
import jakarta.persistence.*;

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

    @OneToOne(mappedBy = "establishment", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private Playlist playlist;

    public Establishment() {
    }

    public Establishment(Long id, String deviceId, boolean isOff, String name) {
        this.id = id;
        this.deviceId = deviceId;
        this.isOff = isOff;
        this.name = name;
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

}

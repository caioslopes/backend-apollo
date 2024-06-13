package br.com.apollo.app.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_establishment")
public class Establishment {

    @Id
    @Column(name = "establishment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String establishmentId;
    private long ownerId;
    private String playlistId;
    private String deviceId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tb_users_establishment", joinColumns = @JoinColumn(name = "establishment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private long userId;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tb_blocked_genres_establishment", joinColumns = @JoinColumn(name = "establishment_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private String blockedGenres;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tb_genres_establishment", joinColumns = @JoinColumn(name = "establishment_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private String genres;
    private boolean isOff;

    public Establishment(String establishmentId, long ownerId, String playlistId, String deviceId, long userId, String blockedGenres, String genres, boolean isOff) {
        this.establishmentId = establishmentId;
        this.ownerId = ownerId;
        this.playlistId = playlistId;
        this.deviceId = deviceId;
        this.userId = userId;
        this.blockedGenres = blockedGenres;
        this.genres = genres;
        this.isOff = isOff;
    }

    public String getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(String establishmentId) {
        this.establishmentId = establishmentId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBlockedGenres() {
        return blockedGenres;
    }

    public void setBlockedGenres(String blockedGenres) {
        this.blockedGenres = blockedGenres;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public boolean isOff() {
        return isOff;
    }

    public void setOff(boolean off) {
        isOff = off;
    }
}

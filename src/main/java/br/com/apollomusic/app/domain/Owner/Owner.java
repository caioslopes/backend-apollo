package br.com.apollomusic.app.domain.Owner;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import jakarta.persistence.*;

@Entity
public class Owner {

    @Id
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String accessToken;

    private int tokenExpiresIn;

    private String refreshToken;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Establishment establishment;

    public Owner() {
    }

    public Owner(Long id, String name, String password, String accessToken, int tokenExpiresIn, String refreshToken) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.accessToken = accessToken;
        this.tokenExpiresIn = tokenExpiresIn;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void setTokenExpiresIn(int tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }
}
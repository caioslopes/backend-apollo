package br.com.apollomusic.app.domain.Owner;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(length = 512)
    private String accessToken;

    private Long tokenExpiresIn;

    private String refreshToken;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Establishment establishment;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "owners_roles",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();

    public Owner() {
    }

    public Owner(Long id, String name, String password, String accessToken, Long tokenExpiresIn, String refreshToken, Establishment establishment, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.accessToken = accessToken;
        this.tokenExpiresIn = tokenExpiresIn;
        this.refreshToken = refreshToken;
        this.establishment = establishment;
        this.roles = roles;
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

    public Long getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void setTokenExpiresIn(Long tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean hasThirdPartyAccess() {
        return refreshToken != null && !refreshToken.isEmpty();
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
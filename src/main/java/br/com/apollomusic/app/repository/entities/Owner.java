package br.com.apollomusic.app.repository.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_owner")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "owner_id")
    private long ownerId;

    @Column(unique = true)
    private String email;
    private String password;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Establishment establishment;

    private String apiAuthCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_owners_roles",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public  Owner() {}

    public Owner(String email, String password, Establishment establishment, String apiAuthCode, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.establishment = establishment;
        this.apiAuthCode = apiAuthCode;
        this.roles = roles;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
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

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public String getApiAuthCode() {
        return apiAuthCode;
    }

    public void setApiAuthCode(String apiAuthCode) {
        this.apiAuthCode = apiAuthCode;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

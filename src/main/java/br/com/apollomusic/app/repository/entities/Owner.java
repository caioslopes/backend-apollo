package br.com.apollomusic.app.repository.entities;

import jakarta.persistence.*;

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


    public Owner(long ownerId, String email, Establishment establishment, String password, String apiAuthCode) {
        this.ownerId = ownerId;
        this.email = email;
        this.establishment = establishment;
        this.password = password;
        this.apiAuthCode = apiAuthCode;
    }

    public Owner() {}

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
}

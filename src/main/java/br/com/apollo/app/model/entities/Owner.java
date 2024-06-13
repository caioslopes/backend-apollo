package br.com.apollo.app.model.entities;

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
    private String establishmentCode;
    private String apiAuthCode;

    public Owner(long id, String email, String password, String establishmentCode, String apiAuthCode) {
        this.ownerId = id;
        this.email = email;
        this.password = password;
        this.establishmentCode = establishmentCode;
        this.apiAuthCode = apiAuthCode;
    }

    public Owner(long id, String email, String password){
        this.ownerId = id;
        this.email = email;
        this.password = password;
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

    public String getEstablishmentCode() {
        return establishmentCode;
    }

    public void setEstablishmentCode(String establishmentCode) {
        this.establishmentCode = establishmentCode;
    }

    public String getApiAuthCode() {
        return apiAuthCode;
    }

    public void setApiAuthCode(String apiAuthCode) {
        this.apiAuthCode = apiAuthCode;
    }
}

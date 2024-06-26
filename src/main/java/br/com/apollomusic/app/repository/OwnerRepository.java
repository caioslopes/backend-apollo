package br.com.apollomusic.app.repository;

import br.com.apollomusic.app.model.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);
    @Modifying
    default void updateRefreshToken(String email, String refreshToken) {
        Owner owner = findByEmail(email).orElseThrow();
        owner.setRefreshToken(refreshToken);
        save(owner);
    }
}

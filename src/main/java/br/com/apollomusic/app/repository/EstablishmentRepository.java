package br.com.apollomusic.app.repository;

import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.model.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {

    @Modifying
    default void setPlaylistEstablishment(long establishmentId, Playlist playlist){
        Establishment establishment = findById(establishmentId).orElseThrow();
        establishment.setPlaylist(playlist);
        save(establishment);
    }

    default Set<User> getUsersEstablishment(Long establishmentId){
        Establishment establishment = findById(establishmentId).orElseThrow();
        return establishment.getUsers();
    }

}

package br.com.apollomusic.app.repository;

import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Playlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {

    @Transactional
    @Modifying
    @Query("update Establishment e set e.playlist = ?1 where e.establishmentId = ?2")
    void setPlaylistEstablishment(Playlist playlist, long establishmentId);

}

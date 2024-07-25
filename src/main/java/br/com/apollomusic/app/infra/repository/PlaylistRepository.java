package br.com.apollomusic.app.infra.repository;

import br.com.apollomusic.app.domain.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
}

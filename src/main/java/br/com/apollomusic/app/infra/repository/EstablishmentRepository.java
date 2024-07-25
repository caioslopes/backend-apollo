package br.com.apollomusic.app.infra.repository;

import br.com.apollomusic.app.domain.entities.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentRepository  extends JpaRepository<Establishment, Long> {
}

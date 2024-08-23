package br.com.apollomusic.app.infra.schedules;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.services.EstablishmentService;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserCleanupScheduler {

    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentService establishmentService;

    public UserCleanupScheduler(EstablishmentRepository establishmentRepository, EstablishmentService establishmentService) {
        this.establishmentRepository = establishmentRepository;
        this.establishmentService = establishmentService;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredUsers() {
        Iterable<Establishment> establishments = establishmentRepository.findAll();

        for (Establishment establishment : establishments) {
            establishmentService.removeUsers(establishment.getId());
        }
    }

}

package br.com.apollomusic.app.infra.schedules;

import br.com.apollomusic.app.application.ApiAuthService;
import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.application.EstablishmentService;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserCleanupScheduler {

    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentService establishmentService;
    private final ApiAuthService apiAuthService;

    public UserCleanupScheduler(EstablishmentRepository establishmentRepository, EstablishmentService establishmentService, ApiAuthService apiAuthService) {
        this.establishmentRepository = establishmentRepository;
        this.establishmentService = establishmentService;
        this.apiAuthService = apiAuthService;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredUsers() {
        Iterable<Establishment> establishments = establishmentRepository.findAll();

        for (Establishment establishment : establishments) {
            apiAuthService.renewTokenIfNeeded(establishment.getOwner().getEmail());
            establishmentService.removeUsers(establishment.getId());
        }
    }

}

package br.com.apollomusic.app.infra.schedules;
import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Establishment.User;
import br.com.apollomusic.app.domain.services.EstablishmentService;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

@Service
public class UserCleanupScheduler {

    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentService establishmentService;

    public UserCleanupScheduler(EstablishmentRepository establishmentRepository, EstablishmentService establishmentService) {
        this.establishmentRepository = establishmentRepository;
        this.establishmentService = establishmentService;
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredUsers() {
        System.out.println("EXECUTOU" );
        Iterable<Establishment> establishments = establishmentRepository.findAll();
        long currentTime = System.currentTimeMillis();

        for (Establishment establishment : establishments) {
            Iterator<User> iterator = establishment.getUser().iterator();

            while (iterator.hasNext()) {
                User user = iterator.next();

                if (user.getExpiresIn() <= currentTime) {
                    Set<String> userGenres = new HashSet<>(Arrays.asList(user.getGenres().split(",")));

                    establishmentService.decrementVoteGenres(establishment.getId(), userGenres);

                    iterator.remove();
                }
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);
            System.out.println("EXECUTOU em: " + formattedTime);


            establishmentRepository.save(establishment);
        }
    }
}

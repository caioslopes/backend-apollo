package br.com.apollomusic.app.infra;

import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.OwnerRepository;
import br.com.apollomusic.app.repository.entities.Establishment;
import br.com.apollomusic.app.repository.entities.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final EstablishmentRepository establishmentRepository;

    @Autowired
    public InitialDataLoader(OwnerRepository ownerRepository, EstablishmentRepository establishmentRepository) {
        this.ownerRepository = ownerRepository;
        this.establishmentRepository = establishmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (ownerRepository.count() == 0 && establishmentRepository.count() == 0) {
            Owner owner = new Owner();
            owner.setEmail("zinho@gmail.com");
            owner.setPassword("123");

            ownerRepository.save(owner);

            Establishment establishment = new Establishment();
            establishment.setOff(false);
            establishment.setOwner(owner);

            establishmentRepository.save(establishment);
        }
    }
}


package br.com.apollomusic.app.infra;

import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.OwnerRepository;
import br.com.apollomusic.app.repository.RoleRepository;
import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Owner;
import br.com.apollomusic.app.model.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
public class InitialDataLoader implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final EstablishmentRepository establishmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public InitialDataLoader(OwnerRepository ownerRepository, EstablishmentRepository establishmentRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.establishmentRepository = establishmentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (ownerRepository.count() == 0 && establishmentRepository.count() == 0) {
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            Owner owner = new Owner();
            owner.setEmail("zinho@gmail.com");
            owner.setPassword(passwordEncoder.encode("123"));
            owner.setRoles(Set.of(roleAdmin));

            ownerRepository.save(owner);

            Establishment establishment = new Establishment();
            establishment.setOff(true);
            establishment.setOwner(owner);

            establishmentRepository.save(establishment);

            Owner owner2 = new Owner();
            owner2.setEmail("adm@gmail.com");
            owner2.setPassword(passwordEncoder.encode("adm"));
            owner2.setRoles(Set.of(roleAdmin));

            ownerRepository.save(owner2);

            Establishment establishment2 = new Establishment();
            establishment2.setOff(true);
            establishment2.setOwner(owner2);

            establishmentRepository.save(establishment2);
        }
    }
}



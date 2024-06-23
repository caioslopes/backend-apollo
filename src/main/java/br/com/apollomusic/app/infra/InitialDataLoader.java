package br.com.apollomusic.app.infra;

import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.OwnerRepository;
import br.com.apollomusic.app.repository.RoleRepository;
import br.com.apollomusic.app.repository.entities.Establishment;
import br.com.apollomusic.app.repository.entities.Owner;
import br.com.apollomusic.app.repository.entities.Role;
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
            establishment.setOff(false);
            establishment.setOwner(owner);

            establishmentRepository.save(establishment);
        }
    }
}



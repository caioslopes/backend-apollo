package br.com.apollomusic.app.application;

import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.Owner.Role;
import br.com.apollomusic.app.domain.payload.request.CreateOwnerRequest;
import br.com.apollomusic.app.domain.payload.response.OwnerResponse;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import br.com.apollomusic.app.infra.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<OwnerResponse> getOwnerByEmail(String email){
        Owner owner = ownerRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OwnerResponse ownerResponse = new OwnerResponse(owner.getName(), owner.getEmail(), owner.hasThirdPartyAccess());
        return new ResponseEntity<>(ownerResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> addOwner(CreateOwnerRequest createOwnerRequest){
        Optional<Owner> owner = ownerRepository.findByEmail(createOwnerRequest.email());
        if(owner.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Owner newOwner = new Owner();
        newOwner.setEmail(createOwnerRequest.email());
        newOwner.setPassword(passwordEncoder.encode(createOwnerRequest.password()));
        newOwner.setRoles(Set.of(roleAdmin));
        ownerRepository.save(newOwner);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.payload.request.AddOwnerRequest;
import br.com.apollomusic.app.domain.payload.response.OwnerResponse;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final EstablishmentRepository establishmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository, EstablishmentRepository establishmentRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.establishmentRepository = establishmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<OwnerResponse> getOwnerByEmail(String email){
        Owner owner = ownerRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OwnerResponse ownerResponse = new OwnerResponse(owner.getName(), owner.getEmail(), owner.hasThirdPartyAccess());
        return new ResponseEntity<>(ownerResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> addOwner(AddOwnerRequest newOwner){
        Optional<Owner> owner = ownerRepository.findByEmail(newOwner.email());
        if(owner.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Optional<Establishment> establishment = establishmentRepository.findById(newOwner.establishmentId());

        if(establishment.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Establishment not found");
        }

        Owner addOwner = new Owner();
        addOwner.setEmail(newOwner.email());
        addOwner.setPassword(passwordEncoder.encode(newOwner.password()));
        addOwner.setEstablishment(establishment.get());
        ownerRepository.save(addOwner);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

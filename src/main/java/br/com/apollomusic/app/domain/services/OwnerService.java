package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.entities.Owner;
import br.com.apollomusic.app.domain.payload.response.OwnerResponse;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository){
        this.ownerRepository = ownerRepository;
    }

    public ResponseEntity<OwnerResponse> getOwnerByEmail(String email){
        Owner owner = ownerRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OwnerResponse ownerResponse = new OwnerResponse(owner.getName(), owner.getEmail(), owner.getRoles(), owner.getRefreshToken());
        return new ResponseEntity<>(ownerResponse, HttpStatus.OK);
    }

}

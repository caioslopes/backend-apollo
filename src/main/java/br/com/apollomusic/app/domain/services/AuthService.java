package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.payload.request.LoginOwnerRequest;
import br.com.apollomusic.app.domain.payload.response.LoginOwnerResponse;
import br.com.apollomusic.app.infra.config.JwtUtil;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AuthService {

    private final EstablishmentRepository establishmentRepository;
    private final JwtUtil jwtUtil;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(EstablishmentRepository establishmentRepository, JwtUtil jwtUtil, OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.establishmentRepository = establishmentRepository;
        this.jwtUtil = jwtUtil;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<LoginOwnerResponse> loginOwner(LoginOwnerRequest loginOwnerRequest) {
        Establishment establishment = findEstablishment(loginOwnerRequest.establishmentId());

        Owner owner = ownerRepository.findByEmail(loginOwnerRequest.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!(Objects.equals(owner.getEstablishment().getId(), establishment.getId()))) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if(!passwordEncoder.matches(loginOwnerRequest.password(), owner.getPassword())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String token = jwtUtil.createTokenOwner(owner);

        return new ResponseEntity<>(new LoginOwnerResponse(token), HttpStatus.OK);
    }

    private Establishment findEstablishment(Long establishmentId) {
        return establishmentRepository.findById(establishmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }

}

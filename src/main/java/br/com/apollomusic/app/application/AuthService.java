package br.com.apollomusic.app.application;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Establishment.User;
import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.payload.request.LoginOwnerRequest;
import br.com.apollomusic.app.domain.payload.request.LoginUserRequest;
import br.com.apollomusic.app.domain.payload.request.LogoutUserRequest;
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
    private final EstablishmentService establishmentService;
    private final ApiAuthService apiAuthService;
    private final JwtUtil jwtUtil;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(EstablishmentRepository establishmentRepository, EstablishmentService establishmentService, JwtUtil jwtUtil, OwnerRepository ownerRepository, PasswordEncoder passwordEncoder, ApiAuthService apiAuthService) {
        this.establishmentRepository = establishmentRepository;
        this.establishmentService = establishmentService;
        this.jwtUtil = jwtUtil;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.apiAuthService = apiAuthService;
    }

    public ResponseEntity<?> loginUser(LoginUserRequest loginUserRequest) {
        Establishment establishment = findEstablishment(loginUserRequest.establishmentId());

        if (establishment.isOff()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        apiAuthService.renewTokenIfNeeded(establishment.getOwner().getEmail());

        establishmentService.incrementVoteGenres(establishment.getId(), loginUserRequest.genres());

        User user = new User();
        user.addGenre(loginUserRequest.genres());

        establishment.addUser(user);
        establishmentRepository.save(establishment);

        String accessToken = jwtUtil.createTokenUser(loginUserRequest);

        return new ResponseEntity<>(new LoginOwnerResponse(accessToken), HttpStatus.CREATED);
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

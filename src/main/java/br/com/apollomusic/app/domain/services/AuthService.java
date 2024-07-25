package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.entities.*;
import br.com.apollomusic.app.domain.payload.request.LoginOwnerRequest;
import br.com.apollomusic.app.domain.payload.request.LoginUserRequest;
import br.com.apollomusic.app.domain.payload.request.LogoutUserRequest;
import br.com.apollomusic.app.domain.payload.response.LoginOwnerResponse;
import br.com.apollomusic.app.domain.payload.response.LoginUserResponse;
import br.com.apollomusic.app.infra.config.JwtUtil;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import br.com.apollomusic.app.infra.repository.RoleRepository;
import br.com.apollomusic.app.infra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, EstablishmentRepository establishmentRepository, JwtUtil jwtUtil, RoleRepository roleRepository, OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.establishmentRepository = establishmentRepository;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<LoginUserResponse> loginUser(LoginUserRequest loginUserRequest) {
        Establishment establishment = findEstablishment(loginUserRequest.establishmentId());

        if(establishment.isOff()) return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User user = new User(establishment, loginUserRequest.username(), new HashSet<>(loginUserRequest.genres()), Set.of(role));
        userRepository.save(user);

        String token = jwtUtil.createTokenUser(user);

        return new ResponseEntity<>(new LoginUserResponse(user.getUsername(), token), HttpStatus.CREATED);
    }

    public ResponseEntity<?> logoutUser(LogoutUserRequest logoutUserRequest){
        User user = userRepository.findById(logoutUserRequest.userId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<String> genres = new HashSet<>(user.getGenres());
        userRepository.deleteById(user.getUserId());

        Establishment establishment = findEstablishment(logoutUserRequest.establishmentId());

        Playlist playlist = establishment.getPlaylist();
        playlist.decrementVoteGenre(genres);

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    public ResponseEntity<LoginOwnerResponse> loginOwner(LoginOwnerRequest loginOwnerRequest) {
        Establishment establishment = findEstablishment(loginOwnerRequest.establishmentId());

        Owner owner = ownerRepository.findByEmail(loginOwnerRequest.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!owner.getEstablishment().equals(establishment)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if(!passwordEncoder.matches(loginOwnerRequest.password(), owner.getPassword())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String token = jwtUtil.createTokenOwner(owner);

        return new ResponseEntity<>(new LoginOwnerResponse(owner.getEmail(), token), HttpStatus.OK);
    }

    private Establishment findEstablishment(Long establishmentId) {
        return establishmentRepository.findById(establishmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }

}

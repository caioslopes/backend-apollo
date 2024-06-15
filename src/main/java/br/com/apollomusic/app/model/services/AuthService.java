package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.infra.JwtUtil;
import br.com.apollomusic.app.model.dto.*;
import br.com.apollomusic.app.repository.OwnerRepository;
import br.com.apollomusic.app.repository.RoleRepository;
import br.com.apollomusic.app.repository.UserRepository;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.entities.Owner;
import br.com.apollomusic.app.repository.entities.Role;
import br.com.apollomusic.app.repository.entities.User;
import br.com.apollomusic.app.repository.entities.Establishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final RoleRepository roleRepository; // Add a RoleRepository to fetch roles
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

    public ResponseEntity<?> loginUser(UserReqDto userReqDto) {
        try {
            Optional<Establishment> establishmentOpt = establishmentRepository.findById(userReqDto.establishmentId());
            Establishment establishment = establishmentOpt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estabelecimento não encontrado"));

            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Papel 'ROLE_USER' não encontrado"));

            User user = new User(establishment, userReqDto.username(), new HashSet<>(userReqDto.genres()), Set.of(userRole));
            userRepository.save(user);

            String token = jwtUtil.createTokenUser(user);
            return ResponseEntity.ok().body(new UserResDto(user.getUserName(), token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> loginOwner(OwnerReqDto ownerReqDto) {
        try {
            Optional<Establishment> establishmentOpt = establishmentRepository.findById(ownerReqDto.establishmentId());
            if (establishmentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            Establishment establishment = establishmentOpt.get();

            Optional<Owner> ownerOpt = ownerRepository.findByEmail(ownerReqDto.email());
            if (ownerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Não foi possível encontrar um usuário com esse email"));
            }

            Owner owner = ownerOpt.get();

            if (!owner.getEstablishment().equals(establishment)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResDto(HttpStatus.UNAUTHORIZED.value(), "Owner não pertence ao estabelecimento especificado"));
            }

            if (!passwordEncoder.matches(ownerReqDto.password(), owner.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResDto(HttpStatus.UNAUTHORIZED.value(), "Senha inválida"));
            }

            String token = jwtUtil.createTokenOwner(owner);
            return ResponseEntity.ok().body(new OwnerResDto(owner.getEmail(), token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }


}

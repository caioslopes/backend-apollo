package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.infra.JwtUtil;
import br.com.apollomusic.app.model.dto.UserReqDto;
import br.com.apollomusic.app.model.dto.UserResDto;
import br.com.apollomusic.app.repository.UserRepository;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.entities.User;
import br.com.apollomusic.app.repository.entities.Establishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, EstablishmentRepository establishmentRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.establishmentRepository = establishmentRepository;
        this.jwtUtil = jwtUtil;
    }

    public UserResDto loginUser(UserReqDto userReqDto) {
        try {
            Optional<Establishment> establishmentOpt = establishmentRepository.findById(userReqDto.establishmentId());
            if (establishmentOpt.isEmpty()) {
                throw new RuntimeException("Establishment not found");
            }

            Establishment establishment = establishmentOpt.get();
            User user = new User(establishment, userReqDto.username(), new HashSet<>(userReqDto.genres()));
            userRepository.save(user);

            String token = jwtUtil.createTokenUser(userReqDto);
            return new UserResDto(user.getUserName(), token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.payload.response.UserReponse;
import br.com.apollomusic.app.infra.repository.UserRepository;
import br.com.apollomusic.app.domain.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserReponse> getUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserReponse userReponse = new UserReponse(user.getUserId(), user.getUsername(), user.getGenres(), user.getEstablishment().getEstablishmentId());
        return new ResponseEntity<>(userReponse, HttpStatus.OK);
    }
}

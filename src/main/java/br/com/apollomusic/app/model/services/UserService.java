package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.dto.User.UserDto;
import br.com.apollomusic.app.model.entities.User;
import br.com.apollomusic.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getUser(Long id){
        try {
            Optional<User> user = userRepository.findById(id);

            if(user.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Usuário não encontrado"));
            }

            UserDto userDto = new UserDto(user.get().getUserId(), user.get().getUsername(), user.get().getGenres());
            return ResponseEntity.ok().body(userDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }
}

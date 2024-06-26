package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.dto.OwnerDto;
import br.com.apollomusic.app.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository){
        this.ownerRepository = ownerRepository;
    }

    public ResponseEntity<?> getOwnerByEmail(String email){
        try {
            var owner = ownerRepository.findByEmail(email);

            if(owner.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Proprietário não encontrado"));
            }

            var ownerDto = new OwnerDto(owner.get().getName(), owner.get().getEmail(), owner.get().getRoles(),owner.get().getRefreshToken());

            return ResponseEntity.ok().body(ownerDto);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }

    }

}

package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EstablishmentService {
    private final EstablishmentRepository establishmentRepository;

    public EstablishmentService(EstablishmentRepository establishmentRepository) {
        this.establishmentRepository = establishmentRepository;
    }

    public ResponseEntity<?> turnOn(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).get();
        if (establishment.isOff()){
            establishment.setOff(false);
            establishmentRepository.save(establishment);
            return ResponseEntity.ok(establishment);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao ligar Estabelicimento");
    }
    public ResponseEntity<?> turnOff(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).get();
        if (!establishment.isOff()){
            establishment.setOff(true);
            establishmentRepository.save(establishment);
            return ResponseEntity.ok(establishment);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao desligar Estabelicimento");
    }
}

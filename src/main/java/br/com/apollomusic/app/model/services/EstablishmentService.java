package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.repository.EstablishmentRepository;


public class EstablishmentService {
    private final EstablishmentRepository establishmentRepository;

    public EstablishmentService(EstablishmentRepository establishmentRepository) {
        this.establishmentRepository = establishmentRepository;
    }

    public boolean turnOn(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).get();
        if (establishment.isOff()){
            establishment.setOff(false);
            establishmentRepository.save(establishment);
            return true;
        }
        return false;
    }
    public boolean turnOff(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).get();
        if (!establishment.isOff()){
            establishment.setOff(true);
            establishmentRepository.save(establishment);
            return true;
        }
        return false;
    }
}

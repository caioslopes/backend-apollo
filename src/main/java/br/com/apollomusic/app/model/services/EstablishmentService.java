package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.dto.Establishment.EstablishmentDto;
import br.com.apollomusic.app.model.dto.Player.DeviceDto;
import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Owner;
import br.com.apollomusic.app.model.entities.User;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstablishmentService {

    @Autowired
    private EstablishmentRepository establishmentRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlayerService playerService;


    public ResponseEntity<?> createPlaylist(long establishmentId, String ownerEmail){
        try {
            Establishment establishment = establishmentRepository.findById(establishmentId).get();
            Owner owner = ownerRepository.findByEmail(ownerEmail).get();
            if(establishment.getEstablishmentId() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.createPlaylist(establishmentId, owner.getAccessToken());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> turnOn(Long establishmentId){
        Establishment establishment;
        try {
            establishment = establishmentRepository.findById(establishmentId).get();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estabelicimento Inexistente");
        }

        if (establishment.isOff()){
            if (establishment.getPlaylist().getVotesQuantity() > 0){
                establishment.setOff(false);
                establishmentRepository.save(establishment);
                return ResponseEntity.status(HttpStatus.OK).body(establishment.getEstablishmentId());
            }
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Selecione Gêneros");

        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Erro ao ligar Estabelicimento");
    }

    public ResponseEntity<?> turnOff(Long establishmentId){
        Establishment establishment;
        try {
            establishment = establishmentRepository.findById(establishmentId).get();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estabelicimento Inexistente");
        }

        if (!establishment.isOff()){
            establishment.setOff(true);
            establishmentRepository.save(establishment);
            return ResponseEntity.status(HttpStatus.OK).body(establishment.getEstablishmentId());
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Erro ao desligar Estabelicimento");
    }

    public ResponseEntity<?> addBlockGenres(long establishmentId, Set<String> genres){
        try{
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.addBlockGenres(establishment.get().getPlaylist().getPlaylistId(), genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> removeBlockGenres(long establishmentId, Set<String> genres){
        try{
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.removeBlockGenres(establishment.get().getPlaylist().getPlaylistId(), genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> incrementVoteGenres(long establishmentId, Set<String> genres){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.incrementVoteGenres(establishment.get().getPlaylist().getPlaylistId(), genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> decrementVoteGenres(long establishmentId, Set<String> genres){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.decrementVoteGenres(establishment.get().getPlaylist().getPlaylistId(), genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> getPlaylist(long establishmentId, String emailOwner){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            Owner ownerInfos = ownerRepository.findByEmail(emailOwner).get();
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.getPlaylist(establishment.get().getPlaylist().getPlaylistId(), ownerInfos.getAccessToken());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> getEstablishment(long establishmentId){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            Set<User> users = establishmentRepository.getUsersEstablishment(establishmentId);
            EstablishmentDto establishmentDto = new EstablishmentDto(establishment.get().getEstablishmentId(), establishment.get().getName(), establishment.get().isOff(), users.size());

            return ResponseEntity.status(HttpStatus.OK).body(establishmentDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> getDevices(long establishmentId, String ownerEmail){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            Owner ownerInfos = ownerRepository.findByEmail(ownerEmail).get();
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playerService.getAvailableDevices(ownerInfos.getAccessToken());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> setMainDevice(long establishmentId, DeviceDto deviceDto){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            if(deviceDto.id().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Corpo da requisição inválido"));
            }

            establishmentRepository.setDeviceId(establishmentId, deviceDto.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(deviceDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }


}

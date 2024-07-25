package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.entities.*;
import br.com.apollomusic.app.domain.payload.request.SetDeviceRequest;
import br.com.apollomusic.app.domain.payload.response.CreatePlaylistResponse;
import br.com.apollomusic.app.domain.payload.response.DeviceResponse;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final OwnerRepository ownerRepository;
    private final ThirdPartyService thirdPartyService;

    @Autowired
    public EstablishmentService(EstablishmentRepository establishmentRepository, OwnerRepository ownerRepository, ThirdPartyService thirdPartyService) {
        this.establishmentRepository = establishmentRepository;
        this.ownerRepository = ownerRepository;
        this.thirdPartyService = thirdPartyService;
    }

    public ResponseEntity<?> turnOn(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!establishment.isOff()){
            establishment.setOff(true);
            establishmentRepository.save(establishment);
            return ResponseEntity.status(HttpStatus.OK).body(establishment.getEstablishmentId());
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Erro ao desligar Estabelicimento");
    }

    public ResponseEntity<?> createPlaylist(long establishmentId, String ownerEmail){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Owner owner = ownerRepository.findByEmail(ownerEmail).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        CreatePlaylistResponse createPlaylistResponse = thirdPartyService.createPlaylist(establishment.getName(), "", owner.getAccessToken());

        Playlist playlist = new Playlist(createPlaylistResponse.id(), new HashSet<>(), new HashSet<>(), new HashMap<>(), createPlaylistResponse.snapshot_id());

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> addSongsToPlaylist(long establishmentId, Set<Song> songs){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Owner owner = establishment.getOwner();
        Playlist playlist = establishment.getPlaylist();

        for (Song s : songs){
            playlist.addSong(s);
        }

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        thirdPartyService.addSongsToPlaylist(playlist.getPlaylistId(), songs, owner.getAccessToken());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> removeSongsFromPlaylist(long establishmentId, Set<Song> songs){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Owner owner = establishment.getOwner();
        Playlist playlist = establishment.getPlaylist();

        for (Song s : songs){
            playlist.removeSong(s);
        }

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        thirdPartyService.removeSongsFromPlaylist(playlist.getPlaylistId(), playlist.getLastSnapshotId(), songs, owner.getAccessToken());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> addBlockGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();
        playlist.addBlockGenres(genres);

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> removeBlockGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();
        playlist.removeBlockGenres(genres);

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> incrementVoteGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();
        playlist.incrementVoteGenre(genres);

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> decrementVoteGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();
        playlist.decrementVoteGenre(genres);

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> getPlaylist(long establishmentId, String emailOwner){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Playlist playlist = establishment.getPlaylist();
        return ResponseEntity.ok(playlist);
    }



    public ResponseEntity<?> getEstablishment(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(establishment);
    }

    public ResponseEntity<?> getDevices(long establishmentId, String ownerEmail){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Owner owner = ownerRepository.findByEmail(ownerEmail).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<String, Collection<DeviceResponse>> devices = thirdPartyService.getDevices(owner.getAccessToken());

        return ResponseEntity.ok(devices);
    }

    public ResponseEntity<?> setMainDevice(long establishmentId, SetDeviceRequest setDeviceRequest){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        establishment.setDeviceId(setDeviceRequest.id());
        establishmentRepository.save(establishment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}

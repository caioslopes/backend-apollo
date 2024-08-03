package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Establishment.Playlist;
import br.com.apollomusic.app.domain.Establishment.Song;
import br.com.apollomusic.app.domain.Establishment.User;
import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.payload.request.LoginUserRequest;
import br.com.apollomusic.app.domain.payload.request.SetDeviceRequest;
import br.com.apollomusic.app.domain.payload.response.*;
import br.com.apollomusic.app.infra.config.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final ThirdPartyService thirdPartyService;

    @Autowired
    public EstablishmentService(EstablishmentRepository establishmentRepository, OwnerRepository ownerRepository, JwtUtil jwtUtil, ThirdPartyService thirdPartyService) {
        this.establishmentRepository = establishmentRepository;
        this.ownerRepository = ownerRepository;
        this.jwtUtil = jwtUtil;
        this.thirdPartyService = thirdPartyService;
    }

    public ResponseEntity<?> turnOn(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (establishment.isOff()){
            if(establishment.getPlaylist() == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            if (establishment.getPlaylist().getVotesQuantity() > 0){
                establishment.setOff(false);
                establishmentRepository.save(establishment);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<?> turnOff(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (establishment.isOff()) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        establishment.setOff(true);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> createPlaylist(long establishmentId, String email){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT));
        CreatePlaylistResponse createPlaylistResponse = thirdPartyService.createPlaylist(establishment.getName(), "", establishment.getOwner().getAccessToken());
        Playlist playlist = new Playlist(createPlaylistResponse.id(), createPlaylistResponse.snapshot_id(), establishment, new HashSet<>(), new HashMap<>(), new HashSet<>());

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public void addSongsToPlaylist(long establishmentId, Set<Song> songs){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Owner owner = establishment.getOwner();
        Playlist playlist = establishment.getPlaylist();

        for (Song s : songs){
            playlist.addSong(s);
        }

        ChangePlaylistResponse changePlaylistResponse = thirdPartyService.addSongsToPlaylist(playlist.getId(), songs, owner.getAccessToken());
        playlist.setSnapshot(changePlaylistResponse.snapshot_id());
        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);
    }

    public void removeSongsFromPlaylist(long establishmentId, Set<Song> songs){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Owner owner = establishment.getOwner();
        Playlist playlist = establishment.getPlaylist();

        for (Song s : songs){
            playlist.removeSong(s);
        }

        ChangePlaylistResponse changePlaylistResponse = thirdPartyService.removeSongsFromPlaylist(playlist.getId(), playlist.getSnapshot(), songs, owner.getAccessToken());
        playlist.setSnapshot(changePlaylistResponse.snapshot_id());
        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);
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

    public ResponseEntity<?> getPlaylist(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Playlist playlist = establishment.getPlaylist();
        if(playlist == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(playlist);
    }

    public ResponseEntity<?> getEstablishment(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new EstablishmentResponse(establishment.getId(), establishment.getDeviceId(), establishment.getName()));
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

    public ResponseEntity<UserReponse> getUser(long establishmentId, Long userId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = establishment.getUser(userId);

        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        UserReponse userReponse = new UserReponse(user.getId(), user.getUsername(), user.getGenres(), establishment.getId());
        return new ResponseEntity<>(userReponse, HttpStatus.OK);
    }

    public ResponseEntity<LoginUserResponse> addUser(LoginUserRequest loginUserRequest){
        Establishment establishment = establishmentRepository.findById(loginUserRequest.establishmentId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User user = new User(loginUserRequest.username(), loginUserRequest.genres(), establishment);

        if(establishment.isOff()) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        establishment.addUser(user);
        establishmentRepository.save(establishment);

        String accessToken = jwtUtil.createTokenUser(user);

        return new ResponseEntity<>(new LoginUserResponse(accessToken), HttpStatus.CREATED);
    }

    public ResponseEntity<?> removeUser(long establishmentId, Long userId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = establishment.getUser(userId);

        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        establishment.removeUser(user.getId());
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

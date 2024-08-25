package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Establishment.Playlist;
import br.com.apollomusic.app.domain.Establishment.User;
import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.payload.request.SetDeviceRequest;
import br.com.apollomusic.app.domain.payload.response.*;
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
    private final AlgorithmService algorithmService;

    @Autowired
    public EstablishmentService(EstablishmentRepository establishmentRepository, OwnerRepository ownerRepository, ThirdPartyService thirdPartyService, AlgorithmService algorithmService) {
        this.establishmentRepository = establishmentRepository;
        this.ownerRepository = ownerRepository;
        this.thirdPartyService = thirdPartyService;
        this.algorithmService = algorithmService;
    }

    public ResponseEntity<?> turnOn(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (establishment.isOff()){
            if(establishment.getPlaylist() == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            if (establishment.getPlaylist().getVotesQuantity() > 0){
                establishment.setOff(false);
                algorithmService.runAlgorithm(establishment);

                establishmentRepository.save(establishment);

                thirdPartyService.setRepeatMode("context", establishment.getDeviceId(), establishment.getOwner().getAccessToken());

                thirdPartyService.setShuffleMode("true", establishment.getDeviceId(), establishment.getOwner().getAccessToken());

                thirdPartyService.startPlayback(establishment.getPlaylist().getUri(), "", establishment.getOwner().getAccessToken());

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<?> turnOff(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (establishment.isOff()) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        establishment.setOff(true);

        AvailableGenresResponse availableGenresResponse = thirdPartyService.getAvailableGenres(establishment.getOwner().getAccessToken());

        Map<String, Integer> genres = new HashMap<>();
        availableGenresResponse.genres().forEach(genre -> genres.put(genre, 0));

        establishment.getPlaylist().setGenres(genres);

        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> createPlaylist(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT));
        CreatePlaylistResponse createPlaylistResponse = thirdPartyService.createPlaylist(establishment.getName(), "", establishment.getOwner().getAccessToken());
        AvailableGenresResponse availableGenresResponse = thirdPartyService.getAvailableGenres(establishment.getOwner().getAccessToken());

        Map<String, Integer> genres = new HashMap<>();
        availableGenresResponse.genres().forEach(genre -> genres.put(genre, 0));

        Playlist playlist = new Playlist(createPlaylistResponse.id(), createPlaylistResponse.snapshot_id(), establishment, new HashSet<>(), genres, new HashSet<>());

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> addBlockGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();
        playlist.addBlockGenres(genres);

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> removeBlockGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();
        playlist.removeBlockGenres(genres);

        establishment.setPlaylist(playlist);
        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> incrementVoteGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();

        if(playlist == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        playlist.incrementVoteGenre(genres);

        establishment.setPlaylist(playlist);

        algorithmService.runAlgorithm(establishment);

        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> decrementVoteGenres(long establishmentId, Set<String> genres){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();

        if(playlist == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        playlist.decrementVoteGenre(genres);

        establishment.setPlaylist(playlist);

        algorithmService.runAlgorithm(establishment);

        establishmentRepository.save(establishment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> getPlaylist(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Playlist playlist = establishment.getPlaylist();
        if(playlist == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        ThirdPartyPlaylistResponse thirdPartyPlaylistResponse = thirdPartyService.getPlaylist(playlist.getId(), establishment.getOwner().getAccessToken());

        PlaylistResponse playlistResponse = new PlaylistResponse(playlist.getId(), thirdPartyPlaylistResponse.name(), thirdPartyPlaylistResponse.description(), thirdPartyPlaylistResponse.images() ,playlist.getBlockedGenres(), playlist.getGenres(), playlist.getVotesQuantity() > 0);
        return ResponseEntity.ok(playlistResponse);
    }

    public ResponseEntity<?> getEstablishment(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Playlist playlist = establishment.getPlaylist();

        ThirdPartyPlaylistResponse thirdPartyPlaylistResponse = thirdPartyService.getPlaylist(playlist.getId(), establishment.getOwner().getAccessToken());

        PlaylistResponse playlistResponse = new PlaylistResponse(playlist.getId(), thirdPartyPlaylistResponse.name(), thirdPartyPlaylistResponse.description(), thirdPartyPlaylistResponse.images() ,playlist.getBlockedGenres(), playlist.getGenres(), playlist.getVotesQuantity() > 0);
        EstablishmentResponse response = new EstablishmentResponse(establishment.getId(), establishment.getName(), establishment.getDeviceId(), establishment.isOff(), playlistResponse);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getEstablishmentUserInfos(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        EstablishmentUserInfosResponse response = new EstablishmentUserInfosResponse(establishment.isOff(), establishment.getName(), establishment.getUser().size());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getAvailableGenres(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT));

        Map<String, Integer> genres =  establishment.getPlaylist().getGenres();

        Integer totalVotes = 0;
        for (Map.Entry<String, Integer> g : genres.entrySet()){
            totalVotes += g.getValue();
        }

        AvailableGenresVotesResponse response = new AvailableGenresVotesResponse(genres, totalVotes);

        return ResponseEntity.ok(response);
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

    public void removeUsers(Long establishmentId) {
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        long currentTime = System.currentTimeMillis();
        List<User> usersCopy = new ArrayList<>(establishment.getUser());
        List<User> remainingUsers = new ArrayList<>();

        for (User user : usersCopy) {
            if (user.getExpiresIn() <= currentTime) {
                Set<String> userGenres = new HashSet<>(Arrays.asList(user.getGenres().split(",")));
                decrementVoteGenres(establishment.getId(), userGenres);
            } else {
                remainingUsers.add(user);
            }
        }

        establishment.setUser(remainingUsers);
        establishmentRepository.save(establishment);
    }

    public ResponseEntity<?> resumePlayback(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (thirdPartyService.getPlaybackState(establishment.getOwner().getAccessToken()).is_playing()){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        thirdPartyService.resumePlayback(establishment.getPlaylist().getUri(), establishment.getOwner().getAccessToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> pausePlayback(Long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!thirdPartyService.getPlaybackState(establishment.getOwner().getAccessToken()).is_playing()){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        thirdPartyService.pausePlayback(establishment.getDeviceId(), establishment.getOwner().getAccessToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

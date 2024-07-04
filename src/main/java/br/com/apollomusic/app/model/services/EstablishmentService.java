package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.Spotify.dto.Me.UserSpotifyDto;
import br.com.apollomusic.app.Spotify.dto.Playlist.NewPlaylistSpotifyDto;
import br.com.apollomusic.app.Spotify.dto.Playlist.NewPlaylistSpotifyResDto;
import br.com.apollomusic.app.Spotify.services.PlaylistSpotifyService;
import br.com.apollomusic.app.Spotify.services.UserSpotifyService;
import br.com.apollomusic.app.Spotify.utils.GenerateDefaultInformation;
import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.dto.Establishment.EstablishmentDto;
import br.com.apollomusic.app.model.dto.NewPlaylistDto;
import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.model.entities.User;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.PlaylistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final PlaylistService playlistService;

    public EstablishmentService(EstablishmentRepository establishmentRepository, PlaylistService playlistService) {
        this.establishmentRepository = establishmentRepository;
        this.playlistService = playlistService;
    }

    public ResponseEntity<?> createPlaylist(long establishmentId, NewPlaylistDto newPlaylistDto){
        try {
            Establishment establishment = establishmentRepository.findById(establishmentId).get();
            if(establishment.getEstablishmentId() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.createPlaylist(establishmentId, newPlaylistDto);
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

    public ResponseEntity<?> getPlaylist(long establishmentId, String spotifyAccessToken){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            return playlistService.getPlaylist(establishment.get().getPlaylist().getPlaylistId(), spotifyAccessToken);
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

}

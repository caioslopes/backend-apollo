package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.Spotify.Dto.Me.UserSpotifyDto;
import br.com.apollomusic.app.Spotify.Dto.Playlist.NewPlaylistSpotifyDto;
import br.com.apollomusic.app.Spotify.Dto.Playlist.NewPlaylistSpotifyResDto;
import br.com.apollomusic.app.Spotify.Services.PlaylistSpotifyService;
import br.com.apollomusic.app.Spotify.Services.UserSpotifyService;
import br.com.apollomusic.app.Spotify.utils.GenerateDefaultInformation;
import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.dto.NewPlaylistDto;
import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.PlaylistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final PlaylistRepository playlistRepository;
    private final UserSpotifyService userSpotifyService;
    private final PlaylistSpotifyService playlistSpotifyService;

    public EstablishmentService(EstablishmentRepository establishmentRepository, PlaylistRepository playlistRepository, UserSpotifyService userSpotifyService, PlaylistSpotifyService playlistSpotifyService) {
        this.establishmentRepository = establishmentRepository;
        this.playlistRepository = playlistRepository;
        this.userSpotifyService = userSpotifyService;
        this.playlistSpotifyService = playlistSpotifyService;
    }

    public ResponseEntity<?> createPlaylistOnSpotify(long id, NewPlaylistDto newPlaylistDto){
        try {
            Establishment establishment = establishmentRepository.findById(id).get();
            if(establishment.getEstablishmentId() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }
            NewPlaylistSpotifyDto newPlaylistSpotifyDto = GenerateDefaultInformation.generateDefaultPlaylist(establishment.getName());
            UserSpotifyDto userSpotifyDto = userSpotifyService.getUserOnSpotify(newPlaylistDto.spotifyAccessToken());
            NewPlaylistSpotifyResDto newPlaylistSpotifyResDto = playlistSpotifyService.createPlaylist(userSpotifyDto.id(), newPlaylistSpotifyDto, newPlaylistDto.spotifyAccessToken());

            Playlist playlist = new Playlist(newPlaylistSpotifyResDto.id(), establishment, new HashSet<>(), new HashSet<>(), new HashMap<>());
            playlistRepository.save(playlist);
            establishmentRepository.setPlaylistEstablishment(playlist, id);

            return ResponseEntity.ok().body("Playlist criada com sucesso: " + newPlaylistSpotifyResDto.id());
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

}

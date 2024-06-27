package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.dto.CreatePlaylistReqDto;
import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.dto.spotify.CreatePlaylistSpotifyReqDto;
import br.com.apollomusic.app.model.dto.spotify.CreatePlaylistSpotifyResDto;
import br.com.apollomusic.app.model.dto.spotify.SpotifyMeResDto;
import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.PlaylistRepository;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final PlaylistRepository playlistRepository;
    private final ApiService apiService;
    private final Gson gson;

    public EstablishmentService(EstablishmentRepository establishmentRepository, PlaylistRepository playlistRepository, ApiService apiService, Gson gson) {
        this.establishmentRepository = establishmentRepository;
        this.playlistRepository = playlistRepository;
        this.apiService = apiService;
        this.gson = gson;
    }

    public ResponseEntity<?> createPlaylist(long id, CreatePlaylistReqDto reqBody){
        try {
            Establishment establishment = establishmentRepository.findById(id).get();

            if(establishment.getEstablishmentId() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            Map<String, String> queryParams = new HashMap<>();
            String me = apiService.get("me", queryParams, reqBody.spotifyAccessToken());
            SpotifyMeResDto spotifyMeResDto = gson.fromJson(me, SpotifyMeResDto.class);

            CreatePlaylistSpotifyReqDto createPlaylistSpotifyReqDto = this.createPlaylistBody(id);

            String endpoint = "/users/" + spotifyMeResDto.id() + "/playlists";
            String postResponse = apiService.postWithResponse(endpoint, createPlaylistSpotifyReqDto, reqBody.spotifyAccessToken());
            CreatePlaylistSpotifyResDto createPlaylistSpotifyResDto = gson.fromJson(postResponse, CreatePlaylistSpotifyResDto.class);

            Playlist playlist = new Playlist(createPlaylistSpotifyResDto.id(), establishment, new HashSet<>(), new HashSet<>(), new HashMap<>() );
            playlistRepository.save(playlist);

            establishmentRepository.setPlaylistEstablishment(playlist, id);

            return ResponseEntity.ok().body("Playlist criada com sucesso: " + createPlaylistSpotifyResDto.id());

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    private CreatePlaylistSpotifyReqDto createPlaylistBody(long establishmentId){
        Establishment establishment = establishmentRepository.findById(establishmentId).get();
        String name = "Playlist do " + establishment.getName();
        String description = "Playlist configurada para tocar em " + establishment.getName();
        return new CreatePlaylistSpotifyReqDto(name, description, false);
    }

}

package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.Spotify.dto.Genre.AvailableGenresDto;
import br.com.apollomusic.app.Spotify.dto.Me.UserSpotifyDto;
import br.com.apollomusic.app.Spotify.dto.Playlist.*;
import br.com.apollomusic.app.Spotify.services.GenreSpotifyService;
import br.com.apollomusic.app.Spotify.services.PlaylistSpotifyService;
import br.com.apollomusic.app.Spotify.services.UserSpotifyService;
import br.com.apollomusic.app.Spotify.utils.GenerateDefaultInformation;
import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.dto.NewPlaylistDto;
import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.model.entities.Song;
import br.com.apollomusic.app.repository.EstablishmentRepository;
import br.com.apollomusic.app.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSpotifyService playlistSpotifyService;
    private final UserSpotifyService userSpotifyService;
    private final GenreSpotifyService genreSpotifyService;
    private final EstablishmentRepository establishmentRepository;

    @Autowired
    public PlaylistService (PlaylistRepository playlistRepository , PlaylistSpotifyService playlistSpotifyService, EstablishmentRepository establishmentRepository, UserSpotifyService userSpotifyService, GenreSpotifyService genreSpotifyService){
        this.playlistRepository = playlistRepository;
        this.playlistSpotifyService = playlistSpotifyService;
        this.establishmentRepository = establishmentRepository;
        this.userSpotifyService = userSpotifyService;
        this.genreSpotifyService = genreSpotifyService;
    }

    public ResponseEntity<?> createPlaylist(long establishmentId, NewPlaylistDto newPlaylistDto){
        try {
            Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
            if(establishment.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Estabelecimento não encontrado"));
            }

            NewPlaylistSpotifyDto newPlaylistSpotifyDto = GenerateDefaultInformation.generateDefaultPlaylist(establishment.get().getName());
            UserSpotifyDto userSpotifyDto = userSpotifyService.getUserOnSpotify(newPlaylistDto.spotifyAccessToken());
            NewPlaylistSpotifyResDto newPlaylistSpotifyResDto = playlistSpotifyService.createPlaylist(userSpotifyDto.id(), newPlaylistSpotifyDto, newPlaylistDto.spotifyAccessToken());

            AvailableGenresDto availableGenres = genreSpotifyService.getAvailableGenres(newPlaylistDto.spotifyAccessToken());
            Map<String, Integer> genres = new HashMap<>();
            for (String genre : availableGenres.genres()){
                genres.put(genre, 0);
            }
            Playlist playlist = new Playlist(newPlaylistSpotifyResDto.id(), establishment.get(), new HashSet<>(), new HashSet<>(), genres, newPlaylistSpotifyResDto.snapshot_id());

            playlistRepository.save(playlist);
            establishmentRepository.setPlaylistEstablishment(establishmentId, playlist);

            return ResponseEntity.ok().body(newPlaylistSpotifyResDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> addSongsToPlaylist(String playlistId, Set<Song> songs, String spotifyAccessToken){
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            if(playlist.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Playlist não encontrada"));
            }

            playlistRepository.addSongsToPlaylist(playlistId, songs);

            Set<String> uris = new HashSet<>();
            for (Song song : songs) {
                uris.add(song.getUri());
            }

            AddItemToPlaylistReqDto addItemToPlaylistReqDto = new AddItemToPlaylistReqDto(uris, 0);
            AddItemToPlaylistResDto addItemToPlaylistResDto = playlistSpotifyService.addItemsToPlaylist(playlistId, addItemToPlaylistReqDto, spotifyAccessToken);

            playlistRepository.setLastSnapshotId(playlistId, addItemToPlaylistResDto.snapshot_id());

            return ResponseEntity.ok().body(addItemToPlaylistResDto);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> removeSongsFromPlaylist(String playlistId, Set<Song> songs, String snapshotId, String spotifyAccessToken){
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            if(playlist.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Playlist não encontrada"));
            }

            playlistRepository.removeSongsFromPlaylist(playlistId, songs);

            Set<ObjectUri> tracks = new HashSet<>();
            for (Song song : songs) {
                tracks.add(new ObjectUri(song.getUri()));
            }

            RemoveItemFromPlaylistReqDto removeItemFromPlaylistReqDto = new RemoveItemFromPlaylistReqDto(tracks, snapshotId);
            RemoveItemFromPlaylistResDto removeItemFromPlaylistResDto = playlistSpotifyService.removeItemsFromPlaylist(playlistId, removeItemFromPlaylistReqDto, spotifyAccessToken);

            playlistRepository.setLastSnapshotId(playlistId, removeItemFromPlaylistResDto.snapshot_id());

            return ResponseEntity.ok().body(removeItemFromPlaylistResDto);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> addBlockGenres(String playlistId, Set<String> genres){
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            if(playlist.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Playlist não encontrada"));
            }

            playlistRepository.addBlockGenre(playlistId, genres);

            return ResponseEntity.ok().body(genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> removeBlockGenres(String playlistId, Set<String> genres){
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            if(playlist.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Playlist não encontrada"));
            }

            playlistRepository.removeBlockGenre(playlistId, genres);

            return ResponseEntity.ok().body(genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> incrementVoteGenres(String playlistId, Set<String> genres){
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            if(playlist.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Playlist não encontrada"));
            }

            playlistRepository.incrementVoteGenres(playlistId, genres);

            return ResponseEntity.ok().body(genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

    public ResponseEntity<?> decrementVoteGenres(String playlistId, Set<String> genres){
        try {
            Optional<Playlist> playlist = playlistRepository.findById(playlistId);

            if(playlist.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResDto(HttpStatus.NOT_FOUND.value(), "Playlist não encontrada"));
            }

            playlistRepository.decrementVoteGenres(playlistId, genres);

            return ResponseEntity.ok().body(genres);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

}

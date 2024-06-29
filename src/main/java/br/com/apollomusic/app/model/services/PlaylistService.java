package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.Spotify.Dto.Playlist.AddItemToPlaylistReqDto;
import br.com.apollomusic.app.Spotify.Dto.Playlist.AddItemToPlaylistResDto;
import br.com.apollomusic.app.Spotify.Services.PlaylistSpotifyService;
import br.com.apollomusic.app.model.dto.ErrorResDto;
import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.model.entities.Song;
import br.com.apollomusic.app.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSpotifyService playlistSpotifyService;

    @Autowired
    public PlaylistService (PlaylistRepository playlistRepository ,PlaylistSpotifyService playlistSpotifyService){
        this.playlistRepository = playlistRepository;
        this.playlistSpotifyService = playlistSpotifyService;
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

            return ResponseEntity.ok().body(addItemToPlaylistResDto);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

}

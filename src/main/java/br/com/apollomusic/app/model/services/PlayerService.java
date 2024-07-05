package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.Spotify.dto.Player.DeviceSpotifyDto;
import br.com.apollomusic.app.Spotify.services.PlayerSpotifyService;
import br.com.apollomusic.app.model.dto.ErrorResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {

    private final PlayerSpotifyService playerSpotifyService;

    public PlayerService(PlayerSpotifyService playerSpotifyService) {
        this.playerSpotifyService = playerSpotifyService;
    }

    public ResponseEntity<?> getAvailableDevices(String spotifyAccessToken){
        try {
            Map<String, Collection<DeviceSpotifyDto>> devices = playerSpotifyService.getAvailableDevices(spotifyAccessToken);
            return ResponseEntity.status(HttpStatus.OK).body(devices);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor"));
        }
    }

}

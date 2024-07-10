package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.Spotify.dto.Player.StartResumePlaybackSpotifyDto;
import br.com.apollomusic.app.model.dto.AddSongsToPlaylistReqDto;
import br.com.apollomusic.app.model.dto.Player.StartResumeDto;
import br.com.apollomusic.app.model.dto.RemoveSongsFromPlaylistReqDto;
import br.com.apollomusic.app.model.dto.Token.SpotifyAccessTokenDto;
import br.com.apollomusic.app.model.services.AlgorithmService;
import br.com.apollomusic.app.model.services.ApiService;
import br.com.apollomusic.app.model.services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    ApiService apiService;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    AlgorithmService algorithmService;

    @GetMapping
    public String hello() {
        return "Hello";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public String admin() {
        return "Admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public String user() {
        return "User";
    }

    @GetMapping("/apiTest/{accessToken}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getGenres(@PathVariable String accessToken) {

        RequestPlayer request = new RequestPlayer("device_id");

        try {
            apiService.post("/me/player/next", request, accessToken);
            return new ResponseEntity<>(ResponseEntity.ok(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/algorithm/{playlistId}")
    public ResponseEntity<?> runAlgorithm(@PathVariable String playlistId, @RequestBody SpotifyAccessTokenDto spotifyAccessTokenDto) {
        return algorithmService.runAlgorithm(playlistId, spotifyAccessTokenDto.spotifyAccessToken());
    }

    @PostMapping("/playlist/{playlistId}/songs")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> addSongsToPlaylist(@PathVariable String playlistId, @RequestBody AddSongsToPlaylistReqDto addSongsToPlaylistReqDto){
        return playlistService.addSongsToPlaylist(playlistId, addSongsToPlaylistReqDto.songs(), addSongsToPlaylistReqDto.spotifyAccessToken());
    }

    @DeleteMapping("/playlist/{playlistId}/songs")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> removeSongsFromPlaylist(@PathVariable String playlistId, @RequestBody RemoveSongsFromPlaylistReqDto removeSongsFromPlaylistReqDto){
        return playlistService.removeSongsFromPlaylist(playlistId, removeSongsFromPlaylistReqDto.songs(), removeSongsFromPlaylistReqDto.snapshot_id(), removeSongsFromPlaylistReqDto.spotifyAccessToken());
    }

    @PutMapping("/player/play")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> startResumePlayback(@RequestBody StartResumeDto startResumeDto) {
        StartResumePlaybackSpotifyDto startResumePlaybackSpotifyDto = new StartResumePlaybackSpotifyDto(startResumeDto.context_uri());
        String endpoint = "/me/player/play?device_id=" + startResumeDto.deviceId();
        return ResponseEntity.ok(apiService.put(endpoint, startResumePlaybackSpotifyDto, startResumeDto.spotifyAccessToken()));
    }

}

class RequestPlayer {
    private String deviceId;

    public RequestPlayer(String device_id) {
        this.deviceId = device_id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}

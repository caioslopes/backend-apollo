package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.dto.Player.DeviceDto;
import br.com.apollomusic.app.model.dto.Token.SpotifyAccessTokenDto;
import br.com.apollomusic.app.model.dto.NewPlaylistDto;
import br.com.apollomusic.app.model.dto.GenreDto;
import br.com.apollomusic.app.model.services.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/establishment/{establishmentId}")
public class EstablishmentController {

    @Autowired
    private EstablishmentService establishmentService;

    @GetMapping
    public ResponseEntity<?> getEstablishment(@PathVariable Long establishmentId) {
        return establishmentService.getEstablishment(establishmentId);
    }

    @PostMapping("/turnOn")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> turnOn(@PathVariable Long establishmentId) {
        return establishmentService.turnOn(establishmentId);
    }

    @PostMapping("/turnOff")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> turnOff(@PathVariable Long establishmentId) {
        return establishmentService.turnOff(establishmentId);
    }

    @GetMapping("/playlist")
    public ResponseEntity<?> getPlaylist(@PathVariable Long establishmentId, @RequestBody SpotifyAccessTokenDto spotifyAccessTokenDto) {
        return establishmentService.getPlaylist(establishmentId, spotifyAccessTokenDto.spotifyAccessToken());
    }

    @PostMapping("/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPlaylist(@PathVariable long establishmentId, @RequestBody NewPlaylistDto newPlaylistDto){
        return establishmentService.createPlaylist(establishmentId, newPlaylistDto);
    }

    @PostMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> addBlockGenres(@PathVariable long establishmentId, @RequestBody GenreDto genreDto){
        return establishmentService.addBlockGenres(establishmentId, genreDto.genres());
    }

    @DeleteMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> removeBlockGenres(@PathVariable long establishmentId, @RequestBody GenreDto genreDto){
        return establishmentService.removeBlockGenres(establishmentId, genreDto.genres());
    }

    @PostMapping("/playlist/genres/increment")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN', 'SCOPE_ROLE_USER')")
    public ResponseEntity<?> incrementVoteGenres(@PathVariable long establishmentId, @RequestBody GenreDto genreDto){
        return establishmentService.incrementVoteGenres(establishmentId, genreDto.genres());
    }

    @PostMapping("/playlist/genres/decrement")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN', 'SCOPE_ROLE_USER')")
    public ResponseEntity<?> decrementVoteGenres(@PathVariable long establishmentId, @RequestBody GenreDto genreDto){
        return establishmentService.decrementVoteGenres(establishmentId, genreDto.genres());
    }

    @GetMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getDevices(@PathVariable Long establishmentId, @RequestBody SpotifyAccessTokenDto spotifyAccessTokenDto) {
        return establishmentService.getDevices(establishmentId, spotifyAccessTokenDto.spotifyAccessToken());
    }

    @PostMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> setMainDevice(@PathVariable Long establishmentId, @RequestBody DeviceDto deviceDto){
        return establishmentService.setMainDevice(establishmentId, deviceDto);
    }

}

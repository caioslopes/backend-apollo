package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.infra.JwtUtil;
import br.com.apollomusic.app.model.dto.Player.DeviceDto;
import br.com.apollomusic.app.model.dto.Token.SpotifyAccessTokenDto;
import br.com.apollomusic.app.model.dto.NewPlaylistDto;
import br.com.apollomusic.app.model.dto.GenreDto;
import br.com.apollomusic.app.model.services.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/establishment")
public class EstablishmentController {

    @Autowired
    private EstablishmentService establishmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getEstablishment(Authentication authentication) {
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.getEstablishment(establishmentId);
    }

    @PostMapping("/turnOn")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> turnOn(Authentication authentication) {
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.turnOn(establishmentId);
    }

    @PostMapping("/turnOff")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> turnOff(Authentication authentication) {
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.turnOff(establishmentId);
    }

    @GetMapping("/playlist")
    public ResponseEntity<?> getPlaylist(Authentication authentication) {
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        String ownerEmail = jwtUtil.extractEmailFromToken(authentication);
        return establishmentService.getPlaylist(establishmentId, ownerEmail);
    }

    @PostMapping("/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPlaylist(Authentication authentication){
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        String ownerEmail = jwtUtil.extractEmailFromToken(authentication);
        return establishmentService.createPlaylist(establishmentId, ownerEmail);
    }

    @PostMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> addBlockGenres(Authentication authentication, @RequestBody GenreDto genreDto){
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.addBlockGenres(establishmentId, genreDto.genres());
    }

    @DeleteMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> removeBlockGenres(Authentication authentication, @RequestBody GenreDto genreDto){
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.removeBlockGenres(establishmentId, genreDto.genres());
    }

    @PostMapping("/playlist/genres/increment")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN', 'SCOPE_ROLE_USER')")
    public ResponseEntity<?> incrementVoteGenres(Authentication authentication, @RequestBody GenreDto genreDto){
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.incrementVoteGenres(establishmentId, genreDto.genres());
    }

    @PostMapping("/playlist/genres/decrement")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN', 'SCOPE_ROLE_USER')")
    public ResponseEntity<?> decrementVoteGenres(Authentication authentication, @RequestBody GenreDto genreDto){
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.decrementVoteGenres(establishmentId, genreDto.genres());
    }

    @GetMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getDevices(Authentication authentication) {
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        String ownerEmail = jwtUtil.extractEmailFromToken(authentication);
        return establishmentService.getDevices(establishmentId, ownerEmail);
    }

    @PostMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> setMainDevice(Authentication authentication, @RequestBody DeviceDto deviceDto){
        Long establishmentId = jwtUtil.extractEstablishmentIdFromToken(authentication);
        return establishmentService.setMainDevice(establishmentId, deviceDto);
    }

}

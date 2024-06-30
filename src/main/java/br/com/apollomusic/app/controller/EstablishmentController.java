package br.com.apollomusic.app.controller;

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

    @PostMapping("/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPlaylist(@PathVariable long establishmentId, @RequestBody NewPlaylistDto newPlaylistDto){
        return establishmentService.createPlaylistOnSpotify(establishmentId, newPlaylistDto);
    }

    @PostMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> blockGenres(@PathVariable long establishmentId, @RequestBody GenreDto genreDto){
        return establishmentService.blockGenres(establishmentId, genreDto.genres());
    }

    @PostMapping("/playlist/genres/increment")
//    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')") we need add both of roles here
    public ResponseEntity<?> incrementVoteGenres(@PathVariable long establishmentId, @RequestBody GenreDto genreDto){
        return establishmentService.incrementVoteGenres(establishmentId, genreDto.genres());
    }

    @PostMapping("/playlist/genres/decrement")
//    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')") we need add both of roles here
    public ResponseEntity<?> decrementVoteGenres(@PathVariable long establishmentId, @RequestBody GenreDto genreDto){
        return establishmentService.decrementVoteGenres(establishmentId, genreDto.genres());
    }

}

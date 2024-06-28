package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.dto.NewPlaylistDto;
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

    @PostMapping("/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPlaylist(@PathVariable long establishmentId, @RequestBody NewPlaylistDto newPlaylistDto){
        return establishmentService.createPlaylistOnSpotify(establishmentId, newPlaylistDto);
    }

}

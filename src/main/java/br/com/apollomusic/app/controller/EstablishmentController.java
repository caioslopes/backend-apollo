package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.dto.CreatePlaylistReqDto;
import br.com.apollomusic.app.model.services.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/establishment")
public class EstablishmentController {

    @Autowired
    private EstablishmentService establishmentService;

    @PostMapping("/{establishmentId}/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPlaylist(@PathVariable long establishmentId, @RequestBody CreatePlaylistReqDto createPlaylistReqDto){
        return establishmentService.createPlaylist(establishmentId, createPlaylistReqDto);
    }

}

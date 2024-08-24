package br.com.apollomusic.app.presentation;

import br.com.apollomusic.app.domain.payload.request.ManipulateGenreRequest;
import br.com.apollomusic.app.domain.payload.request.SetDeviceRequest;
import br.com.apollomusic.app.infra.config.JwtUtil;
import br.com.apollomusic.app.domain.services.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/establishment")
public class EstablishmentController {

    @Autowired
    private EstablishmentService establishmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<?> getEstablishment(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getEstablishment(establishmentId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEstablishmentById(@PathVariable Long id) {
        return establishmentService.getEstablishment(id);
    }

    @PostMapping("/turn-on")
    public ResponseEntity<?> turnOn(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.turnOn(establishmentId);
    }

    @PostMapping("/turn-off")
    public ResponseEntity<?> turnOff(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.turnOff(establishmentId);
    }

    @GetMapping("/playlist")
    public ResponseEntity<?> getPlaylist(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getPlaylist(establishmentId);
    }

    @PostMapping("/playlist")
    public ResponseEntity<?> createPlaylist(Authentication authentication){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.createPlaylist(establishmentId);
    }
    @GetMapping("/playlist/genres/{establishmentId}")
    public ResponseEntity<?> getAvailableGenres(@PathVariable Long establishmentId){
         return establishmentService.getAvailableGenres(establishmentId);
    }

    @PutMapping("/playlist/genres/block")
    public ResponseEntity<?> addBlockGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.addBlockGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PutMapping("/playlist/genres/unblock")
    public ResponseEntity<?> removeBlockGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.removeBlockGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PostMapping("/playlist/genres/increment")
    public ResponseEntity<?> incrementVoteGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.incrementVoteGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PostMapping("/playlist/genres/decrement")
    public ResponseEntity<?> decrementVoteGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.decrementVoteGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @GetMapping("/devices")
    public ResponseEntity<?> getDevices(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return establishmentService.getDevices(establishmentId, ownerEmail);
    }

    @PostMapping("/devices")
    public ResponseEntity<?> setMainDevice(Authentication authentication, @RequestBody SetDeviceRequest setDeviceRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.setMainDevice(establishmentId, setDeviceRequest);
    }

}

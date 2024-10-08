package br.com.apollomusic.app.presentation;

import br.com.apollomusic.app.domain.payload.request.CreateEstablishmentRequest;
import br.com.apollomusic.app.domain.payload.request.ManipulateGenreRequest;
import br.com.apollomusic.app.domain.payload.request.SetDeviceRequest;
import br.com.apollomusic.app.infra.config.JwtUtil;
import br.com.apollomusic.app.application.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${secret.key}")
    private String SECRET_KEY;

    @PostMapping("/new")
    public ResponseEntity<?> createEstablishment(@RequestHeader("X-Secret-Key") String secretKey, @RequestBody CreateEstablishmentRequest createEstablishmentRequest){
        if(!SECRET_KEY.equals(secretKey))  return ResponseEntity.status(403).body("Forbidden: Invalid secret key");
        return establishmentService.createEstablishment(createEstablishmentRequest);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getEstablishment(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getEstablishment(establishmentId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEstablishmentById(@PathVariable Long id) {
        return establishmentService.getEstablishmentUserInfos(id);
    }

    @PostMapping("/turn-on")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> turnOn(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.turnOn(establishmentId);
    }

    @PostMapping("/turn-off")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> turnOff(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.turnOff(establishmentId);
    }

    @GetMapping("/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getPlaylist(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getPlaylist(establishmentId);
    }

    @PostMapping("/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPlaylist(Authentication authentication){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.createPlaylist(establishmentId);
    }
    @GetMapping("/playlist/genres/{establishmentId}")
    public ResponseEntity<?> getAvailableGenres(@PathVariable Long establishmentId){
         return establishmentService.getAvailableGenres(establishmentId);
    }

    @PutMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> addBlockGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.addBlockGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PutMapping("/playlist/genres/unblock")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> removeBlockGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.removeBlockGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PostMapping("/playlist/genres/increment")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> incrementVoteGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.incrementVoteGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PostMapping("/playlist/genres/decrement")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> decrementVoteGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.decrementVoteGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PutMapping("/playlist/genres/initial")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> setPlaylistInitialGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.setPlaylistInitialGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @GetMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getDevices(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return establishmentService.getDevices(establishmentId, ownerEmail);
    }

    @GetMapping("/player")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getPlaybackState(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getPlaybackState(establishmentId);
    }

    @PostMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> setMainDevice(Authentication authentication, @RequestBody SetDeviceRequest setDeviceRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.setMainDevice(establishmentId, setDeviceRequest);
    }

    @PutMapping("/player/resume")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> resumePlayer(Authentication authentication){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.resumePlayback(establishmentId);
    }

    @PutMapping("/player/pause")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> pausePlayer(Authentication authentication){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.pausePlayback(establishmentId);
    }

    @PostMapping("/player/next")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> skipToNext(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.skipToNext(establishmentId);
    }

    @PostMapping("/player/previous")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> skipToPrevious(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.skipToPrevious(establishmentId);
    }

}

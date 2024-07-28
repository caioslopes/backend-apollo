package br.com.apollomusic.app.presentation;

import br.com.apollomusic.app.domain.payload.request.ManipulateGenreRequest;
import br.com.apollomusic.app.domain.payload.request.SetDeviceRequest;
import br.com.apollomusic.app.infra.config.JwtUtil;
import br.com.apollomusic.app.domain.Establishment.services.EstablishmentService;
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
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getEstablishment(establishmentId);
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
    public ResponseEntity<?> getPlaylist(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getPlaylist(establishmentId);
    }

    @PostMapping("/playlist")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> createPlaylist(Authentication authentication){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return establishmentService.createPlaylist(establishmentId, ownerEmail);
    }

    @PostMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> addBlockGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.addBlockGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @DeleteMapping("/playlist/genres/block")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> removeBlockGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.removeBlockGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PostMapping("/playlist/genres/increment")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN', 'SCOPE_ROLE_USER')")
    public ResponseEntity<?> incrementVoteGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.incrementVoteGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @PostMapping("/playlist/genres/decrement")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN', 'SCOPE_ROLE_USER')")
    public ResponseEntity<?> decrementVoteGenres(Authentication authentication, @RequestBody ManipulateGenreRequest manipulateGenreRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.decrementVoteGenres(establishmentId, manipulateGenreRequest.genres());
    }

    @GetMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getDevices(Authentication authentication) {
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return establishmentService.getDevices(establishmentId, ownerEmail);
    }

    @PostMapping("/devices")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> setMainDevice(Authentication authentication, @RequestBody SetDeviceRequest setDeviceRequest){
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.setMainDevice(establishmentId, setDeviceRequest);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(Authentication authentication) {
        Long userID = jwtUtil.extractItemFromToken(authentication, "userId");
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        return establishmentService.getUser(establishmentId, userID);
    }

//    @PostMapping("/user")
//    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
//        return authService.loginUser(loginUserRequest);
//    }
//
//    @DeleteMapping("/user")
//    public ResponseEntity<?> logoutUser(Authentication authentication) {
//        Long userId = jwtUtil.extractItemFromToken(authentication, "userId");
//        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
//        LogoutUserRequest logoutUserRequest = new LogoutUserRequest(userId, establishmentId);
//        return  authService.logoutUser(logoutUserRequest);
//    }

}

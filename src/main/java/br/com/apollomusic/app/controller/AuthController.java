package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.dto.LogoutUserDto;
import br.com.apollomusic.app.model.dto.OwnerApiAuthReqDto;
import br.com.apollomusic.app.model.dto.OwnerReqDto;
import br.com.apollomusic.app.model.dto.UserReqDto;
import br.com.apollomusic.app.model.services.ApiAuthService;
import br.com.apollomusic.app.model.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ApiAuthService apiAuthService;

    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@RequestBody UserReqDto user) {
        return authService.loginUser(user);
    }

    @PostMapping("/owner")
    public ResponseEntity<?> loginOwner(@RequestBody OwnerReqDto ownerReqDto) {
        return authService.loginOwner(ownerReqDto);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> logoutUser(@RequestBody LogoutUserDto logoutUserDto) {
        return  authService.logoutUser(logoutUserDto);
    }

    @PostMapping("/api")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> authApi(@RequestBody OwnerApiAuthReqDto reqDto) {
        return apiAuthService.getAccessTokenFromApi(reqDto);
    }

    @PostMapping("/api/renew")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> renewApi(@RequestBody OwnerApiAuthReqDto reqDto) {
        return apiAuthService.renewAccessToken(reqDto);
    }

}

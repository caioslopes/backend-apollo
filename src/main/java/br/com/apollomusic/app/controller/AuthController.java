package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.infra.JwtUtil;
import br.com.apollomusic.app.model.dto.LogoutUserDto;
import br.com.apollomusic.app.model.dto.OwnerApiAuthReqDto;
import br.com.apollomusic.app.model.dto.OwnerReqDto;
import br.com.apollomusic.app.model.dto.UserReqDto;
import br.com.apollomusic.app.model.services.ApiAuthService;
import br.com.apollomusic.app.model.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ApiAuthService apiAuthService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@RequestBody UserReqDto user) {
        return authService.loginUser(user);
    }

    @PostMapping("/owner")
    public ResponseEntity<?> loginOwner(@RequestBody OwnerReqDto ownerReqDto) {
        return authService.loginOwner(ownerReqDto);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        Long userId = jwtUtil.extractItemFromToken(authentication, "userId");
        Long establishmentId = jwtUtil.extractItemFromToken(authentication, "establishmentId");
        LogoutUserDto logoutUserDto = new LogoutUserDto(userId, establishmentId);
        return  authService.logoutUser(logoutUserDto);
    }

    @PostMapping("/api")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> authApi(Authentication authentication, @RequestBody OwnerApiAuthReqDto reqDto) {
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return apiAuthService.getAccessTokenFromApi(reqDto, ownerEmail);
    }

}

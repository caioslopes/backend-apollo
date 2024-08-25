package br.com.apollomusic.app.presentation;

import br.com.apollomusic.app.domain.payload.request.AuthorizeThirdPartyRequest;
import br.com.apollomusic.app.domain.payload.request.LoginOwnerRequest;
import br.com.apollomusic.app.domain.payload.request.LoginUserRequest;
import br.com.apollomusic.app.domain.payload.request.LogoutUserRequest;
import br.com.apollomusic.app.infra.config.JwtUtil;
import br.com.apollomusic.app.application.ApiAuthService;
import br.com.apollomusic.app.application.AuthService;
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

    @PostMapping("/owner")
    public ResponseEntity<?> loginOwner(@RequestBody LoginOwnerRequest loginOwnerRequest) {
        return authService.loginOwner(loginOwnerRequest);
    }

    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        return authService.loginUser(loginUserRequest);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> logoutUser(@RequestBody LogoutUserRequest logoutUserRequest) {
        return authService.logoutUser(logoutUserRequest);
    }

    @PostMapping("/api")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> authApi(Authentication authentication, @RequestBody AuthorizeThirdPartyRequest authorizeThirdPartyRequest) {
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return apiAuthService.getAccessTokenFromApi(authorizeThirdPartyRequest, ownerEmail);
    }

}

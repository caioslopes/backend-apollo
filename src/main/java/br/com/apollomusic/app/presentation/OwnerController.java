package br.com.apollomusic.app.presentation;

import br.com.apollomusic.app.domain.payload.request.AddOwnerRequest;
import br.com.apollomusic.app.application.OwnerService;
import br.com.apollomusic.app.infra.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("secret.key")
    private String SECRET_KEY;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getOwner(Authentication authentication) {
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return ownerService.getOwnerByEmail(ownerEmail);
    }

    @PostMapping
    public ResponseEntity<?> createOwner(@RequestHeader("X-Secret-Key") String secretKey,
                                         @RequestBody AddOwnerRequest addOwnerRequest) {
        if (!SECRET_KEY.equals(secretKey)) {
            return ResponseEntity.status(403).body("Forbidden: Invalid secret key");
        }
        return ownerService.addOwner(addOwnerRequest);
    }
}


package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.infra.JwtUtil;
import br.com.apollomusic.app.model.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping()
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getOwner(Authentication authentication) {
        String ownerEmail = jwtUtil.extractItemFromToken(authentication, "email");
        return ownerService.getOwnerByEmail(ownerEmail);
    }
}

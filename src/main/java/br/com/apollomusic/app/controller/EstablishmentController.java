package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.services.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{establishmentId}")
public class EstablishmentController {
    @Autowired
    private EstablishmentService establishmentService;


    @PostMapping("/turnOn")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> turnOn(@PathVariable Long establishmentId) {
        return establishmentService.turnOn(establishmentId);
    }

        @PostMapping("/turnOff")
        @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
        public ResponseEntity<?> turnOff(@PathVariable Long establishmentId) {
            return establishmentService.turnOff(establishmentId);
        }

}

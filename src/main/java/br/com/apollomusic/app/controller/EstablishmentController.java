package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.entities.Establishment;
import br.com.apollomusic.app.model.services.EstablishmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/establishment")
public class EstablishmentController {
    EstablishmentService establishmentService;


    @PostMapping("/turnOn")
    public boolean turnOn(Long establishmentId) {
        return establishmentService.turnOn(establishmentId);
    }

    @PostMapping("/turnOff")
    public boolean turnOff(Long establishmentId) {
        return establishmentService.turnOff(establishmentId);
    }

}

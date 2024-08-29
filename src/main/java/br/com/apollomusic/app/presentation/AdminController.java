package br.com.apollomusic.app.presentation;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.payload.admin.SimpleEstablishmentResponse;
import br.com.apollomusic.app.domain.payload.admin.SimpleOwnerResponse;
import br.com.apollomusic.app.infra.repository.EstablishmentRepository;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final EstablishmentRepository establishmentRepository;
    private final OwnerRepository ownerRepository;

    @Value("${secret.key}")
    private String SECRET_KEY;

    @Autowired
    public AdminController(EstablishmentRepository establishmentRepository, OwnerRepository ownerRepository){
        this.establishmentRepository = establishmentRepository;
        this.ownerRepository = ownerRepository;
    }

    @GetMapping("/establishment")
    public ResponseEntity<?> getAllEstablishmentIds(@RequestHeader("X-Secret-Key") String secretKey){
        if (!SECRET_KEY.equals(secretKey)) return ResponseEntity.status(403).body("Forbidden: Invalid secret key");

        List<SimpleEstablishmentResponse> info = new ArrayList<>();
        List<Establishment> establishments = establishmentRepository.findAll();
        establishments.forEach(est -> info.add(new SimpleEstablishmentResponse(est.getId(),est.getName(),est.getOwner().getEmail())));

        return ResponseEntity.ok(info);
    }

    @GetMapping("/owner")
    public  ResponseEntity<?> getAllOwners(@RequestHeader("X-Secret-Key") String secretKey){
        if (!SECRET_KEY.equals(secretKey)) return ResponseEntity.status(403).body("Forbidden: Invalid secret key");

        List<SimpleOwnerResponse> info = new ArrayList<>();
        List<Owner> owners = ownerRepository.findAll();
        owners.forEach(o -> info.add(new SimpleOwnerResponse(o.getId(),o.getName(),o.getEmail())));

        return ResponseEntity.ok(info);
    }

}

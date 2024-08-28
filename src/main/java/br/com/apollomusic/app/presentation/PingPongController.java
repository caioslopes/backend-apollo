package br.com.apollomusic.app.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingPongController {

    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Pong!");
    }

}

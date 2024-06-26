package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.services.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    ApiService apiService;

    @GetMapping
    public String hello() {
        return "Hello";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public String admin() {
        return "Admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public String user() {
        return "User";
    }

    @GetMapping("/apiTest/{accessToken}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<?> getGenres(@PathVariable String accessToken) {

        RequestPlayer request = new RequestPlayer("device_id");

        try {
            apiService.post("/me/player/next", request, accessToken);
            return new ResponseEntity<>(ResponseEntity.ok(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

class RequestPlayer {
    private String deviceId;

    public RequestPlayer(String device_id) {
        this.deviceId = device_id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}

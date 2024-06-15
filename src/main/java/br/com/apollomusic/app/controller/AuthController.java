package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.dto.OwnerReqDto;
import br.com.apollomusic.app.model.dto.UserReqDto;
import br.com.apollomusic.app.model.dto.UserResDto;
import br.com.apollomusic.app.model.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@RequestBody UserReqDto user) {
        return authService.loginUser(user);
    }

    @PostMapping("/owner")
    public ResponseEntity<?> loginOwner(@RequestBody OwnerReqDto ownerReqDto) {
        return authService.loginOwner(ownerReqDto);
    }

}

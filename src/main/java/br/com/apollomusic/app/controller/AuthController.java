package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.model.dto.UserReqDto;
import br.com.apollomusic.app.model.dto.UserResDto;
import br.com.apollomusic.app.model.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/user")
    public ResponseEntity<UserResDto> loginUser(@RequestBody UserReqDto user) {
        var result = authService.loginUser(user);
        return ResponseEntity.ok().body(result);
    }
}

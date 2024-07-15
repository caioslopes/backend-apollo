package br.com.apollomusic.app.controller;

import br.com.apollomusic.app.infra.JwtUtil;
import br.com.apollomusic.app.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/{userID}")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getUser(Authentication authentication) {
        Long userID = jwtUtil.extractItemFromToken(authentication, "userId");
        return userService.getUser(userID);
    }

}

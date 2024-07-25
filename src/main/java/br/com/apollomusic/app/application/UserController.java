package br.com.apollomusic.app.application;

import br.com.apollomusic.app.domain.services.UserService;
import br.com.apollomusic.app.infra.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
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

package br.com.apollomusic.app.infra;

import br.com.apollomusic.app.repository.entities.Owner;
import br.com.apollomusic.app.repository.entities.Role;
import br.com.apollomusic.app.repository.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    private static final long accessTokenValidity = 60 * 1000;

    public String createTokenUser(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUserName());
        claims.put("establishmentId", user.getEstablishment().getEstablishmentId());
        claims.put("genres", user.getGenres());
        claims.put("scope", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public String createTokenOwner(Owner owner) {
        Claims claims = Jwts.claims().setSubject(owner.getEmail());
        claims.put("establishmentId", owner.getEstablishment().getEstablishmentId());
        claims.put("scope", owner.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

}

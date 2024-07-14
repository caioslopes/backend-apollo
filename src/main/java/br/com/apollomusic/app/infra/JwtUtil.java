package br.com.apollomusic.app.infra;

import br.com.apollomusic.app.model.entities.Owner;
import br.com.apollomusic.app.model.entities.Role;
import br.com.apollomusic.app.model.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    private static final long accessTokenValidity = 60;

    public String createTokenUser(User user) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId());
        claims.put("establishmentId", user.getEstablishment().getEstablishmentId());
        claims.put("scope", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity * 2));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public String createTokenOwner(Owner owner) {
        Claims claims = Jwts.claims();
        claims.put("ownerId", owner.getOwnerId());
        claims.put("establishmentId", owner.getEstablishment().getEstablishmentId());
        claims.put("scope", owner.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        claims.put("email", owner.getEmail());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity * 20));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public String extractEmailFromToken(Authentication authentication) {
        JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = jwtAuthToken.getTokenAttributes();
        return (String) claims.get("email");
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

}

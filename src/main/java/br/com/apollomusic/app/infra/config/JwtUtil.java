package br.com.apollomusic.app.infra.config;

import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.Establishment.User;
import io.jsonwebtoken.Claims;
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

@Component
public class JwtUtil {

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    private static final long accessTokenValidity = 60;

    public String createTokenUser(User user) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());
//        claims.put("establishmentId", user.getEstablishment().getEstablishmentId());
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
        claims.put("ownerId", owner.getId());
        claims.put("establishmentId", owner.getEstablishment().getId());
//        claims.put("scope", owner.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        claims.put("email", owner.getEmail());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity * 20));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public <T> T extractItemFromToken(Authentication authentication, String item) {
        JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = jwtAuthToken.getTokenAttributes();
        return (T) claims.get(item);
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

}

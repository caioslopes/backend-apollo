package br.com.apollomusic.app.infra;

import br.com.apollomusic.app.repository.entities.Owner;
import br.com.apollomusic.app.repository.entities.Role;
import br.com.apollomusic.app.repository.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.private.key}")
    private String privateKey;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final long accessTokenValidity = 60 * 1000;

    public String createTokenUser(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUserName());
        claims.put("establishmentId", user.getEstablishment().getEstablishmentId());
        claims.put("genres", user.getGenres());
        claims.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    public String createTokenOwner(Owner owner) {
        Claims claims = Jwts.claims().setSubject(owner.getEmail());
        claims.put("establishmentId", owner.getEstablishment().getEstablishmentId());
        claims.put("roles", owner.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    public String getClaims(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}

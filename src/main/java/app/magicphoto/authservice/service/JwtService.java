package app.magicphoto.authservice.service;

import app.magicphoto.authservice.model.dao.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    @Getter
    private Long jwtExpirationTime;

    private SecretKey signInKey;

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(CustomUser user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, CustomUser user) {
        return buildToken(extraClaims, user, jwtExpirationTime);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              CustomUser user,
                              Long expirationTime) {
        var now=Instant.now();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getLogin())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationTime)))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, CustomUser user) {
        final String login = extractLogin(token);
        return (login.equals(user.getLogin())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        if (signInKey == null) {
            byte[] keyBytes=Decoders.BASE64.decode(secretKey);
            signInKey=Keys.hmacShaKeyFor(keyBytes);
        }
        return signInKey;
    }
}

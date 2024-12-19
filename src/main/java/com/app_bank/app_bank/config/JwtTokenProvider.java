package com.app_bank.app_bank.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${secret-jwt-authentication}")
    private String jwtSecret;

    @Value("${jwt-expiration-date}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication)
    {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    public SecretKey key()
    {
        byte[] bytes = Decoders.BASE64.decode(this.jwtSecret);

        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUsername(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String token)
    {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);

            return true;

        } catch (ExpiredJwtException | IllegalArgumentException | SecurityException | MalformedJwtException e) {

            throw new RuntimeException(e);
        }

    }
}

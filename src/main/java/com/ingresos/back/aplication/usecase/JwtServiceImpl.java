package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;

    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtServiceImpl(
            @Value("${jwt.access.secret}") String accessSecret,
            @Value("${jwt.access.expiration}") long accessExpiration,

            @Value("${jwt.refresh.secret}") String refreshSecret,
            @Value("${jwt.refresh.expiration}") long refreshExpiration
    ) {

        this.accessSecretKey = Keys.hmacShaKeyFor(
                accessSecret.getBytes(StandardCharsets.UTF_8)
        );

        this.refreshSecretKey = Keys.hmacShaKeyFor(
                refreshSecret.getBytes(StandardCharsets.UTF_8)
        );

        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public String generateAccessToken(User user) {

        return generateToken(
                user,
                accessSecretKey,
                accessExpiration,
                "access"
        );
    }

    @Override
    public String generateRefreshToken(User user) {

        return generateToken(
                user,
                refreshSecretKey,
                refreshExpiration,
                "refresh"
        );
    }

    private String generateToken(
            User user,
            SecretKey secretKey,
            long expiration,
            String type
    ) {

        Date now = new Date();

        Date expirationDate = new Date(
                now.getTime() + expiration
        );

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", type)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String getEmailFromAccessToken(String token) {

        return getClaims(
                token,
                accessSecretKey
        ).getSubject();
    }

    @Override
    public String getEmailFromRefreshToken(String token) {

        return getClaims(
                token,
                refreshSecretKey
        ).getSubject();
    }

    @Override
    public boolean isAccessTokenValid(String token) {

        try {
            Claims claims = getClaims(
                    token,
                    accessSecretKey
            );

            return "access".equals(
                    claims.get("type", String.class)
            );

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isRefreshTokenValid(String token) {

        try {
            Claims claims = getClaims(
                    token,
                    refreshSecretKey
            );

            return "refresh".equals(
                    claims.get("type", String.class)
            );

        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(
            String token,
            SecretKey secretKey
    ) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

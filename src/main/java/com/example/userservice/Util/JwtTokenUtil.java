package com.example.userservice.Util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private final static int EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    public Key getSigningKey() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(this.secret.getBytes());
        return Keys.hmacShaKeyFor(hash);
    }
    public String generateToken(Map<String, String> map) throws NoSuchAlgorithmException {
        byte[] content = "Hello World".getBytes(StandardCharsets.UTF_8);
        return Jwts.builder()
                .header()
                .and()
                .claims(map)
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(getSigningKey())
                .compact();

    }


}

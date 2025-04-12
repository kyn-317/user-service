package com.kyn.user.base.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.kyn.user.base.config.JwtConfig;
import com.kyn.user.base.dto.JwtRequestDto;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long tokenValidationDate;
    private static final String AUTHORITIES_KEY = "auth";

    public JwtTokenProvider(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.tokenValidationDate = jwtConfig.getExpiration();
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidationDate);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(secretKey)
                .expiration(validity)
                .compact();
    }

    public String createToken(JwtRequestDto dto) {
        return this.createToken(dto, this.tokenValidationDate);
    }
    public String createToken(JwtRequestDto dto , Long expirationTime){
        return Jwts.builder()
                .subject(dto.getEmail())
                .claim(AUTHORITIES_KEY, dto.getAuthorities())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.getPayload().get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getPayload().getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

}
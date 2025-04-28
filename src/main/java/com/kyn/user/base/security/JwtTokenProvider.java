package com.kyn.user.base.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.kyn.commonjwt.dto.TokenDto;
import com.kyn.commonjwt.dto.TokenRequest;
import com.kyn.commonjwt.service.JwtService;
import com.kyn.user.base.dto.JwtRequestDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    
    private static final String AUTHORITIES_KEY = "auth";
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7일
    private final JwtService jwtService;

    public JwtTokenProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public TokenDto createToken(TokenRequest request) {
        return jwtService.generateToken(request);
    }
    
    public String createAccessTokenFromRefreshToken(String refreshToken) {
        if (jwtService.validateToken(refreshToken)) {
            Claims claims = jwtService.getClaims(refreshToken);
            String email = claims.getSubject();
            String authorities = claims.get(AUTHORITIES_KEY).toString();
            
            TokenRequest request = TokenRequest.builder()
            .subject(email)
            .roles(Arrays.asList(authorities))
            .build();
            return jwtService.generateJustToken(request);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtService.getClaims(token);

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Claims getClaims(String token) {
        return jwtService.getClaims(token);
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public String getEmailFromToken(String token) {
        if (jwtService.validateToken(token)) {
            return jwtService.getClaims(token).getSubject();
        }
        return null;
    }
}
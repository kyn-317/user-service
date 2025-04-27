package com.kyn.user.base.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.kyn.commonjwt.dto.TokenDto;
import com.kyn.commonjwt.dto.TokenRequest;
import com.kyn.commonjwt.service.JwtService;

import io.jsonwebtoken.Claims;

@Component
public class JwtTokenProvider {

    private final JwtService jwtService;
    public JwtTokenProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }



    public String createToken(TokenRequest request) {
        return jwtService.generateJustToken(request);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtService.getClaims(token);

        Collection<? extends GrantedAuthority> authorities = 
                jwtService.getRoles(token)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
       return jwtService.validateToken(token);
    }

    public Claims getClaims(String token) {
        return jwtService.getClaims(token);
    }

}
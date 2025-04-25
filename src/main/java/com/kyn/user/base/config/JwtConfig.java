package com.kyn.user.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kyn.commonjwt.service.JwtService;

@Configuration
public class JwtConfig {


    @Bean
    public JwtService jwtService(
        @Value("${jwt.secret}") String secret) {
        return new JwtService(secret);
    }
}

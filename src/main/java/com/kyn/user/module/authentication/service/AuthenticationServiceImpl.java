package com.kyn.user.module.authentication.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyn.commonjwt.dto.TokenRequest;
import com.kyn.user.base.dto.ResponseDto;
import com.kyn.user.base.exception.InvalidCredentialsException;
import com.kyn.user.base.exception.InvalidTokenException;
import com.kyn.user.base.security.JwtTokenProvider;
import com.kyn.user.module.authentication.service.interfaces.AuthenticationService;
import com.kyn.user.module.user.dto.UserInfoDto;
import com.kyn.user.module.user.service.interfaces.UserSearchService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserSearchService userSearchService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RMapCacheReactive<String, Boolean> jwtBlacklistMap;

    public AuthenticationServiceImpl( PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                                 RedissonClient redissonClient, UserSearchService userSearchService) {
        this.passwordEncoder = passwordEncoder;
        this.userSearchService = userSearchService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtBlacklistMap = redissonClient.reactive().getMapCache("blacklist:id");
    }

    @Override
    public Mono<String> login(UserInfoDto userInfoDto) {
        return userSearchService.findLoginUser(userInfoDto.getEmail())
            .switchIfEmpty(Mono.error(new InvalidCredentialsException()))
            .flatMap(authenticationUserDto -> {
                if (!passwordEncoder.matches(userInfoDto.getPassword(), authenticationUserDto.getPassword())) {
                    return Mono.error(new InvalidCredentialsException());
                }

                List<SimpleGrantedAuthority> authorities = authenticationUserDto.getUserAuths().stream()
                .map(auth -> new SimpleGrantedAuthority("ROLE_" + auth.getRole().name()))
                .collect(Collectors.toList());
                var jwtRequest = TokenRequest.builder()
                .roles(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .subject(authenticationUserDto.getEmail()).build();
                return Mono.<String>just(jwtTokenProvider.createToken(jwtRequest));
        });
    }

    @Override
    public Mono<ResponseDto<String>> isLogin(String token) {
        return Mono.just(token)
            .flatMap(t -> {
                if (!jwtTokenProvider.validateToken(t)) {
                    return Mono.error(new InvalidTokenException());
                }
                return jwtBlacklistMap.get(t)
                    .flatMap(isBlacklisted -> {
                        if (Boolean.TRUE.equals(isBlacklisted)) return Mono.error(new InvalidTokenException());
                        return Mono.just(ResponseDto.create("LOGIN", "login success", HttpStatus.OK));
                    })
                    .defaultIfEmpty(ResponseDto.create("LOGIN", "login success", HttpStatus.OK));
            });
    }

    @Override
    public Mono<ResponseDto<String>> logout(String token) {
            if (!jwtTokenProvider.validateToken(token)) {
                    return Mono.error(new InvalidTokenException());
            }

            log.info("token: {}", jwtTokenProvider.getClaims(token).getExpiration().getTime()
            - System.currentTimeMillis());
            return jwtBlacklistMap.fastPut(token, true,
                         jwtTokenProvider.getClaims(token).getExpiration().getTime()
                          - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .map(result -> ResponseDto.create("SUCCESS LOGOUT", "logout success", HttpStatus.OK));
    }

    @Override
    public Mono<ResponseDto<Long>> getExpirationTime(String token) {
        return Mono.just(token)
            .flatMap(t -> jwtBlacklistMap.remainTimeToLive(t)
                .flatMap(ttl -> {
                    if (ttl > 0) {
                        // ttl이 있으면 블랙리스트에 있는 토큰
                        log.debug("Token is blacklisted and will expire in {} seconds", ttl);
                        return Mono.just(ResponseDto.create(
                            ttl, 
                            "블랙리스트에 있는 토큰의 남은 만료 시간(초)", 
                            HttpStatus.OK
                        ));
                    } else {
                        // ttl이 없으면 블랙리스트에 없거나 이미 만료된 토큰
                        log.debug("Token is not in blacklist or already expired");
                        return Mono.just(ResponseDto.create(
                            0L, 
                            "토큰이 블랙리스트에 없거나 이미 만료됨", 
                            HttpStatus.OK
                        ));
                    }
                })
            );
    }
    
}

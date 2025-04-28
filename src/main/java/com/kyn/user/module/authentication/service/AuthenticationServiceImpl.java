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

import com.kyn.commonjwt.dto.TokenDto;
import com.kyn.commonjwt.dto.TokenRequest;
import com.kyn.commonjwt.service.JwtService;
import com.kyn.user.base.dto.ResponseDto;
import com.kyn.user.base.exception.InvalidCredentialsException;
import com.kyn.user.base.exception.InvalidTokenException;
import com.kyn.user.base.security.JwtTokenProvider;
import com.kyn.user.module.authentication.dto.UserResponseDto;
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
    public Mono<TokenDto> login(UserInfoDto userInfoDto) {
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
                    .subject(authenticationUserDto.getEmail())
                    .build();
                return Mono.just(jwtTokenProvider.createToken(jwtRequest));
            });
    }

    @Override
    public Mono<UserResponseDto> isLogin(String token) {
        log.info("token: {}", token);
        return Mono.just(token)
            .flatMap(t -> {
                if (!jwtTokenProvider.validateToken(t)) {
                    return Mono.error(new InvalidTokenException());
                }
                return jwtBlacklistMap.get(t)
                    .flatMap(isBlacklisted -> {
                        if (Boolean.TRUE.equals(isBlacklisted)) return Mono.error(new InvalidTokenException());
                        return getUser(token);
                    })
                    .switchIfEmpty(getUser(token));
            });
    }

    private Mono<UserResponseDto> getUser(String token){
        return userSearchService.findLoginUser(jwtTokenProvider.getClaims(token).getSubject())
            .map(user -> new UserResponseDto(user.getUserName(), user.getEmail()));
    }

    @Override
    public Mono<String> logout(String token) {
        return Mono.just(token)
            .filter(jwtTokenProvider::validateToken)
            .switchIfEmpty(Mono.error(new InvalidTokenException()))
            .flatMap(validToken -> {
                long expirationTimeMillis = jwtTokenProvider.getClaims(validToken).getExpiration().getTime() - System.currentTimeMillis();
                return jwtBlacklistMap.fastPut(validToken, true, expirationTimeMillis, TimeUnit.MILLISECONDS)
                    .map(result -> "SUCCESS LOGOUT");
            });
    }

    @Override
    public Mono<Long> getExpirationTime(String token) {
        return Mono.just(token)
            .flatMap(t -> jwtBlacklistMap.remainTimeToLive(t)
                .flatMap(ttl -> {
                    if (ttl > 0) {
                        // if has ttl, token is blacklisted
                        log.debug("Token is blacklisted and will expire in {} seconds", ttl);
                        return Mono.just(ttl);
                    } else {
                        // if ttl is 0, token is not in blacklist or already expired
                        log.debug("Token is not in blacklist or already expired");
                        return Mono.just(0L);
                    }
                })
            );
    }
    
}

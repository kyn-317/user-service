package com.kyn.user.module.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyn.user.base.dto.JwtRequestDto;
import com.kyn.user.base.dto.ResponseDto;
import com.kyn.user.base.enums.Role;
import com.kyn.user.base.exception.InvalidTokenException;
import com.kyn.user.base.security.JwtTokenProvider;
import com.kyn.user.module.dto.UserAuthDto;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.exception.InvalidCredentialsException;
import com.kyn.user.module.repository.UserAuthRepository;
import com.kyn.user.module.repository.UserInfoRepository;
import com.kyn.user.module.service.interfaces.AuthenticationService;
import com.kyn.user.module.service.interfaces.UserSearchService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import reactor.core.publisher.Mono;

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
                var jwtRequestDto = JwtRequestDto.create(authenticationUserDto.getEmail(), authorities);
                return Mono.<String>just(jwtTokenProvider.createToken(jwtRequestDto));
        });
    }

    @Override
    public Mono<ResponseDto<String>> isLogin(String token) {
            return Mono.just(token)
                            .flatMap(t -> (!jwtTokenProvider.validateToken(t) || jwtBlacklistMap.get(t).block())
                                            ? Mono.error(new InvalidTokenException())
                                            : Mono.just(ResponseDto.create("SUCCESS LOGIN",
                                                            "login success", HttpStatus.OK)));
    }

    @Override
    public Mono<ResponseDto<String>> logout(String token) {
            if (!jwtTokenProvider.validateToken(token)) {
                    return Mono.error(new InvalidTokenException());
            }

            Jws<Claims> claims = jwtTokenProvider.getClaims(token);
            long ttl = (claims.getPayload().getExpiration().getTime()
                            - System.currentTimeMillis()) / 1000;

            return jwtBlacklistMap.fastPut(token, true, ttl, TimeUnit.SECONDS)
                            .map(result -> ResponseDto.create("SUCCESS LOGOUT", "logout success", HttpStatus.OK));
    }
    
}

package com.kyn.user.module.service;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyn.user.base.dto.ResponseDto;
import com.kyn.user.base.exception.InvalidTokenException;
import com.kyn.user.base.security.JwtTokenProvider;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.exception.InvalidCredentialsException;
import com.kyn.user.module.exception.UserNotFoundException;
import com.kyn.user.module.repository.UserAuthRepository;
import com.kyn.user.module.repository.UserInfoRepository;
import com.kyn.user.module.service.interfaces.AuthenticationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserInfoRepository userInfoRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RMapCacheReactive<String, Boolean> jwtBlacklistMap;

    public AuthenticationServiceImpl(UserInfoRepository userInfoRepository, UserAuthRepository userAuthRepository,
     PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RedissonClient redissonClient) {
        this.userInfoRepository = userInfoRepository;
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtBlacklistMap = redissonClient.reactive().getMapCache("blacklist:id");
    }

    @Override
    public Mono<ResponseDto<String>> login(UserInfoDto userInfoDto) {
            return userInfoRepository.findByEmail(userInfoDto.getEmail())
                            .switchIfEmpty(Mono.error(new UserNotFoundException()))
                            .flatMap(userInfo -> {
                                    if (!passwordEncoder.matches(userInfoDto.getPassword(),
                                                                userInfo.getPassword())) {
                                            return Mono.error(new InvalidCredentialsException());
                                    }

                                    return userAuthRepository.findByUserInfoId(userInfo.getUserInfoId())
                                                    .collectList()
                                                    .flatMap(auths -> {
                                                            Authentication authentication = new UsernamePasswordAuthenticationToken(
                                                                            userInfo.getUserId(),
                                                                            null,
                                                                            auths.stream()
                                                                                            .map(auth -> new SimpleGrantedAuthority(
                                                                                                            auth.getRole().toString()))
                                                                                            .collect(Collectors
                                                                                                            .toList()));

                                                            String token = jwtTokenProvider
                                                                            .createToken(authentication);

                                                            return Mono.just(ResponseDto.create(token,
                                                                            "loginSuccess",
                                                                            HttpStatus.OK));
                                                    });
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

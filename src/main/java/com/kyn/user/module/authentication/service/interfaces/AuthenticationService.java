package com.kyn.user.module.authentication.service.interfaces;

import com.kyn.commonjwt.dto.TokenDto;
import com.kyn.user.module.authentication.dto.UserResponseDto;
import com.kyn.user.module.user.dto.UserInfoDto;

import reactor.core.publisher.Mono;

public interface AuthenticationService {

    public Mono<TokenDto> login(UserInfoDto userInfoDto);
    public Mono<UserResponseDto> isLogin(String token);
    public Mono<String> logout(String token);
    public Mono<Long> getExpirationTime(String token);
}

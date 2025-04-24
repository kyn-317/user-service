package com.kyn.user.module.authentication.service.interfaces;

import com.kyn.user.base.dto.ResponseDto;
import com.kyn.user.module.user.dto.UserInfoDto;

import reactor.core.publisher.Mono;

public interface AuthenticationService {

    public Mono<String> login(UserInfoDto userInfoDto);
    public Mono<ResponseDto<String>> isLogin(String token);
    public Mono<ResponseDto<String>> logout(String token);
    public Mono<ResponseDto<Long>> getExpirationTime(String token);
}

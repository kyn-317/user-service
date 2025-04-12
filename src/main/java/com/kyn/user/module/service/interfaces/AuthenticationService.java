package com.kyn.user.module.service.interfaces;

import com.kyn.user.base.dto.ResponseDto;
import com.kyn.user.module.dto.UserInfoDto;

import reactor.core.publisher.Mono;

public interface AuthenticationService {

    public Mono<String> login(UserInfoDto userInfoDto);
    public Mono<ResponseDto<String>> isLogin(String token);
    public Mono<ResponseDto<String>> logout(String token);
    
}

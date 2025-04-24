package com.kyn.user.module.user.service.interfaces;

import com.kyn.user.module.user.dto.UserInfoDto;

import reactor.core.publisher.Mono;

public interface UserService {
    //user create and update 
    public Mono<UserInfoDto> createUser(UserInfoDto userInfoDto);
    public Mono<UserInfoDto> updateUser(UserInfoDto userInfoDto);
    public Mono<Boolean> isValidUser(UserInfoDto userInfoDto);
    
}

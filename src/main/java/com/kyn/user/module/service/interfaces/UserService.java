package com.kyn.user.module.service.interfaces;

import java.util.UUID;

import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.dto.UserRequestDto;


import reactor.core.publisher.Mono;

public interface UserService {
    //user create and update 
    public Mono<UserInfoDto> createUser(UserInfoDto userInfoDto);
    public Mono<UserInfoDto> updateUser(UserInfoDto userInfoDto);
    
}

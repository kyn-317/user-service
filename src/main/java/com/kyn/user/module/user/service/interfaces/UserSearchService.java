package com.kyn.user.module.user.service.interfaces;

import java.util.UUID;

import com.kyn.user.module.authentication.dto.AuthenticationUserDto;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.management.dto.UserResponseDto;

import reactor.core.publisher.Mono;

public interface UserSearchService {

    public Mono<UserResponseDto> findUserById(UUID id);
    public Mono<UserResponseDto> findUserByEmail(String email);
    public Mono<UserResponseDto> findUserByUserId(String userId);
    public Mono<UserResponseDto> findUserByUserName(String userName);
    public Mono<UserResponseDto> findUserByDto(UserRequestDto dto);
    public Mono<Boolean> isExistUser(String email);
    public Mono<AuthenticationUserDto> findLoginUser(String email); 
    
}

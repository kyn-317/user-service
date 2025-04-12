package com.kyn.user.module.service.interfaces;

import java.util.UUID;

import com.kyn.user.module.dto.AuthenticationUserDto;

import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;

import reactor.core.publisher.Mono;

public interface UserSearchService {

    public Mono<UserResponseDto> findUserById(UUID id);
    public Mono<UserResponseDto> findUserByEmail(String email);
    public Mono<UserResponseDto> findUserByUserId(String userId);
    public Mono<UserResponseDto> findUserByUserName(String userName);
    public Mono<UserResponseDto> findUserByDto(UserRequestDto dto);
    public Mono<AuthenticationUserDto> findLoginUser(String email); 
    
}

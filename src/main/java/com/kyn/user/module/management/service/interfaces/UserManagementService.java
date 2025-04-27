package com.kyn.user.module.management.service.interfaces;

import com.kyn.commonjwt.dto.TokenDto;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.management.dto.UserResponseDto;

import reactor.core.publisher.Mono;

public interface UserManagementService {
    
    public Mono<UserResponseDto> createUser(UserRequestDto dto);    
    public Mono<UserResponseDto> updateUser(UserRequestDto dto);
    public Mono<UserResponseDto> addRole(UserRequestDto dto);
    public Mono<UserResponseDto> deleteRole(UserRequestDto dto);
    public Mono<UserResponseDto> searchUser(UserRequestDto dto);
    public Mono<Boolean> isValidUser(UserRequestDto dto);
    public Mono<TokenDto> login(UserRequestDto dto);
}

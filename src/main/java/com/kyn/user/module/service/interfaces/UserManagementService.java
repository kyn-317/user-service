package com.kyn.user.module.service.interfaces;

import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;

import reactor.core.publisher.Mono;

public interface UserManagementService {
    
    public Mono<UserResponseDto> createUser(UserRequestDto dto);    
    public Mono<UserResponseDto> updateUser(UserRequestDto dto);
    public Mono<UserResponseDto> addRole(UserRequestDto dto);
    public Mono<UserResponseDto> deleteRole(UserRequestDto dto);
    public Mono<UserResponseDto> searchUser(UserRequestDto dto);

}

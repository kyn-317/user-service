package com.kyn.user.module.service.interfaces;

import java.util.UUID;

import javax.management.relation.Role;

import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;

import reactor.core.publisher.Mono;

public interface UserManagementService {
    

    public Mono<UserResponseDto> createUser(UserRequestDto dto);    
    public Mono<UserResponseDto> updateUser(UserRequestDto dto);
    public Mono<UserResponseDto> addRole(UserRequestDto dto);
}

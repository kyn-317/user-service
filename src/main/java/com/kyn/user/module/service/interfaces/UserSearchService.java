package com.kyn.user.module.service.interfaces;

import java.util.UUID;

import com.kyn.user.module.dto.UserResponseDto;

import reactor.core.publisher.Mono;

public interface UserSearchService {
    public Mono<UserResponseDto> findUserById(UUID id);
    public Mono<UserResponseDto> findUserByEmail(String email);
    public Mono<UserResponseDto> findUserByUserId(String userId);
}

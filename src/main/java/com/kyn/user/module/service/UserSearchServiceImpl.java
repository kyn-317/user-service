package com.kyn.user.module.service;

import java.util.UUID;

import com.kyn.user.module.dto.UserResponseDto;
import com.kyn.user.module.service.interfaces.UserSearchService;

import reactor.core.publisher.Mono;

public class UserSearchServiceImpl implements UserSearchService {

    @Override
    public Mono<UserResponseDto> findUserById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUserById'");
    }

    @Override
    public Mono<UserResponseDto> findUserByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUserByEmail'");
    }

    @Override
    public Mono<UserResponseDto> findUserByUserId(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUserByUserId'");
    }
    
}

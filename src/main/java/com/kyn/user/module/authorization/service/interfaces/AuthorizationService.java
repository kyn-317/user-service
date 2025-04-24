package com.kyn.user.module.authorization.service.interfaces;

import java.util.UUID;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authorization.dto.UserAuthDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorizationService {

    public Mono<UserAuthDto> addUserAuth(UserAuthDto dto);
    public Mono<Void> removeUserAuth(UUID userId, Role role);
    public Flux<UserAuthDto> getUserAuths(UUID userId);
    
}

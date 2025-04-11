package com.kyn.user.module.service.interfaces;

import java.util.UUID;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.dto.UserAuthDto;
import com.kyn.user.module.dto.UserInfoDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorizationService {
    public Mono<UserAuthDto> addUserAuth(UUID userId, Role role);
    public Mono<UserInfoDto> removeUserAuth(UUID userId, Role role);
    public Flux<UserAuthDto> getUserAuths(UUID userId);
}

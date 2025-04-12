package com.kyn.user.module.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.service.interfaces.AuthenticationService;
import com.kyn.user.module.service.interfaces.UserManagementService;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final AuthenticationService authenticationService;
    private final UserManagementService userManagementService;

    public UserHandler(UserManagementService userManagementService, AuthenticationService authenticationService) {
        this.userManagementService = userManagementService;
        this.authenticationService = authenticationService;
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDto.class)
                .flatMap(userManagementService::createUser)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return Mono.empty();
        /* request.bodyToMono(UserRequestDto.class)
                .flatMap(authenticationService::login)
                .flatMap(user -> ServerResponse.ok().bodyValue(user)); */
    }

    public Mono<ServerResponse> addRole(ServerRequest request) {
        return request.bodyToMono(UserRequestDto.class)
                .flatMap(userManagementService::addRole)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }
}

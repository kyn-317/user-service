package com.kyn.user.module.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.service.interfaces.AuthenticationService;
import com.kyn.user.module.service.interfaces.AuthorizationService;
import com.kyn.user.module.service.interfaces.UserService;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;

    public UserHandler(IUserService userService, IAuthenticationService authenticationService, IAuthorizationService authorizationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserInfoDto.class)
                .flatMap(userService::createUser)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(UserInfoDto.class)
                .flatMap(authenticationService::login)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }
}

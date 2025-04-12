package com.kyn.user.module.handler;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.service.interfaces.AuthenticationService;
import com.kyn.user.module.service.interfaces.UserManagementService;
import com.kyn.user.module.service.interfaces.UserSearchService;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final AuthenticationService authenticationService;
    private final UserManagementService userManagementService;
    private final UserSearchService userSearchService;

    public UserHandler(UserManagementService userManagementService, AuthenticationService authenticationService, UserSearchService userSearchService) {
        this.userManagementService = userManagementService;
        this.authenticationService = authenticationService;
        this.userSearchService = userSearchService;
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

    public Mono<ServerResponse> searchUserByEmail(ServerRequest request) {
        var email = request.pathVariable("email");
        return userSearchService.findUserByEmail(email)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

    public Mono<ServerResponse> searchUserByUserId(ServerRequest request) {
        var userId = request.pathVariable("userId");
        return userSearchService.findUserByUserId(userId)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

    public Mono<ServerResponse> searchUserByUserName(ServerRequest request) {
        var userName = request.pathVariable("userName");
        return userSearchService.findUserByUserName(userName)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

    public Mono<ServerResponse> searchUserById(ServerRequest request) {
        var id = request.pathVariable("id");
        return userSearchService.findUserById(UUID.fromString(id))
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }
}

package com.kyn.user.handler;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.management.service.interfaces.UserManagementService;
import com.kyn.user.module.authentication.service.interfaces.AuthenticationService;
import com.kyn.user.module.user.service.interfaces.UserSearchService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
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
        return request.bodyToMono(UserRequestDto.class)
                .flatMap(userManagementService::login)
                .flatMap(user -> {
                    // Create HTTP-only cookie with refresh token
                    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", user.getRefreshToken())
                            .httpOnly(true)
                            .secure(true)  // HTTPS only
                            .path("/")
                            .maxAge(7 * 24 * 60 * 60)  // 7 days
                            .build();
                    return ServerResponse.ok()
                            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                            .bodyValue(user);
                });
    }

    public Mono<ServerResponse> addRole(ServerRequest request) {
        return request.bodyToMono(UserRequestDto.class)
                .flatMap(userManagementService::addRole)
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .onErrorResume(e -> {
                    log.error("Error adding role: {}", e.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(e.getMessage());
                });
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

    public Mono<ServerResponse> isLogin(ServerRequest request) {
        log.info("headers :{} ", request.headers().toString());
        String accessToken = request.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        log.info("accessToken :{} ", accessToken);
        return authenticationService.isLogin(accessToken)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        log.info("headers :{} ", request.headers().toString());
        String authHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ServerResponse.badRequest().bodyValue("Authorization header is missing or invalid");
        }
        String accessToken = authHeader.substring(7);
        log.info("accessToken :{} ", accessToken);
        return authenticationService.logout(accessToken)
                .flatMap(user -> {
                    // Clear refresh token cookie
                    ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(0)
                            .build();

                    return ServerResponse.ok()
                            .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                            .bodyValue(user);
                });
    }

    public Mono<ServerResponse> getExpirationTime(ServerRequest request) {
        return request.bodyToMono(String.class)
                .flatMap(authenticationService::getExpirationTime)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

}

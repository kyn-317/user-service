package com.kyn.user.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kyn.user.handler.UserHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserRouter {
    
    private final UserHandler userHandler;
    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions.route(RequestPredicates.POST("/user/create"), this.userHandler::createUser)
                .andRoute(RequestPredicates.POST("/user/login"), this.userHandler::login)
                .andRoute(RequestPredicates.POST("/user/addrole"), this.userHandler::addRole)
                .andRoute(RequestPredicates.GET("/user/searchEmail/{email}"), this.userHandler::searchUserByEmail)
                .andRoute(RequestPredicates.GET("/user/searchUserId/{userId}"), this.userHandler::searchUserByUserId)
                .andRoute(RequestPredicates.GET("/user/searchUserName/{userName}"), this.userHandler::searchUserByUserName)
                .andRoute(RequestPredicates.GET("/user/searchId/{id}"), this.userHandler::searchUserById)
                .andRoute(RequestPredicates.POST("/user/login"), this.userHandler::login)
                .andRoute(RequestPredicates.POST("/user/logout"), this.userHandler::logout)
                .andRoute(RequestPredicates.POST("/user/isLogin"), this.userHandler::isLogin)
                .andRoute(RequestPredicates.POST("/user/getExpirationTime"), this.userHandler::getExpirationTime);
    }
    
}

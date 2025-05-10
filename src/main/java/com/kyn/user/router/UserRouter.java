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
        return RouterFunctions.route(RequestPredicates.POST("/create"), this.userHandler::createUser)
                .andRoute(RequestPredicates.POST("/login"), this.userHandler::login)
                .andRoute(RequestPredicates.POST("/logout"), this.userHandler::logout)
                .andRoute(RequestPredicates.POST("/isLogin"), this.userHandler::isLogin)
                .andRoute(RequestPredicates.POST("/addRole"), this.userHandler::addRole)
                .andRoute(RequestPredicates.GET("/searchEmail/{email}"), this.userHandler::searchUserByEmail)
                .andRoute(RequestPredicates.GET("/searchUserId/{userId}"), this.userHandler::searchUserByUserId)
                .andRoute(RequestPredicates.GET("/searchUserName/{userName}"), this.userHandler::searchUserByUserName)
                .andRoute(RequestPredicates.GET("/searchId/{id}"), this.userHandler::searchUserById)
                .andRoute(RequestPredicates.POST("/getExpirationTime"), this.userHandler::getExpirationTime);
    }
    
}

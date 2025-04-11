package com.kyn.user.module.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kyn.user.module.handler.UserHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserRouter {
    
    private final UserHandler userHandler;
    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions.route(RequestPredicates.POST("/user/create"), this.userHandler::createUser)
                .andRoute(RequestPredicates.POST("/user/login"), this.userHandler::login);
    }
    
}

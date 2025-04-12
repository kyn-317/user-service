package com.kyn.user.module.service;

import org.springframework.stereotype.Service;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.dto.UserEntityDtoUtil;
import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;
import com.kyn.user.module.mapper.UserInfoEntityDtoMapper;
import com.kyn.user.module.mapper.UserManagementDtoMapper;
import com.kyn.user.module.service.interfaces.AuthorizationService;
import com.kyn.user.module.service.interfaces.UserManagementService;
import com.kyn.user.module.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserService userService;
    private final AuthorizationService authorizationService;

    public UserManagementServiceImpl(UserService userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @Override
    public Mono<UserResponseDto> createUser(UserRequestDto dto) {
        var userInfoDto = UserManagementDtoMapper.userRequestDtoToUserInfoDto(dto);
        return userService.createUser(userInfoDto)
                .flatMap(userInfo -> 
                    authorizationService.addUserAuth(UserManagementDtoMapper.userInfoDtoToUserAuthDto(userInfo, Role.USER))
                            .map(savedAuth -> UserInfoEntityDtoMapper
                            .userInfoEntityWithAuthDtoToUserResponseDto
                                (UserEntityDtoUtil.dtoToEntity(userInfo), Collections.singletonList(savedAuth))));
    }

    @Override
    public Mono<UserResponseDto> updateUser(UserRequestDto dto){
        var userInfoDto = UserManagementDtoMapper.userRequestDtoToUserInfoDto(dto);
        return userService.updateUser(userInfoDto)
                .map(UserManagementDtoMapper::userInfoDtoToUserResponseDto);
    }

    @Override
    public Mono<UserResponseDto> addRole(UserRequestDto dto){
        var userAuthDto = UserManagementDtoMapper.userRequestDtoToUserAuthDto(dto);
        log.info("Adding role to user: {}", userAuthDto);
        return authorizationService.addUserAuth(userAuthDto)
                .map(UserManagementDtoMapper::userAuthDtoToUserResponseDto);
    }
}

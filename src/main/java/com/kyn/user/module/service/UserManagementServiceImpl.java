package com.kyn.user.module.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyn.user.base.dto.ResponseDto;
import com.kyn.user.base.enums.Role;
import com.kyn.user.module.dto.UserEntityDtoUtil;
import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;
import com.kyn.user.module.mapper.UserInfoEntityDtoMapper;
import com.kyn.user.module.mapper.UserManagementDtoMapper;
import com.kyn.user.module.service.interfaces.AuthorizationService;
import com.kyn.user.module.service.interfaces.UserManagementService;
import com.kyn.user.module.service.interfaces.UserSearchService;
import com.kyn.user.module.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final UserSearchService userSearchService;
    public UserManagementServiceImpl(UserService userService, AuthorizationService authorizationService, UserSearchService userSearchService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.userSearchService = userSearchService;
    }

    @Override
    @Transactional
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
        return authorizationService.addUserAuth(userAuthDto)
                .then(Mono.empty());
    }

    @Override
    public Mono<UserResponseDto> deleteRole(UserRequestDto dto){
        var userAuthDto = UserManagementDtoMapper.userRequestDtoToUserAuthDto(dto);
        return authorizationService.removeUserAuth(userAuthDto.getUserInfoId(), userAuthDto.getRole())
                .then(Mono.empty());
    }

    @Override
    public Mono<UserResponseDto> searchUser(UserRequestDto dto){
        return userSearchService.findUserByDto(dto);
    }
}

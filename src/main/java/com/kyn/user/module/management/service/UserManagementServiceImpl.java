package com.kyn.user.module.management.service;

import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyn.commonjwt.dto.TokenDto;
import com.kyn.user.base.enums.Role;
import com.kyn.user.base.exception.AlreadySignedUserException;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.management.dto.UserResponseDto;
import com.kyn.user.module.management.mapper.UserManagementDtoMapper;
import com.kyn.user.module.user.mapper.UserInfoEntityDtoMapper;
import com.kyn.user.module.authentication.service.interfaces.AuthenticationService;
import com.kyn.user.module.authorization.service.interfaces.AuthorizationService;
import com.kyn.user.module.management.service.interfaces.UserManagementService;
import com.kyn.user.module.user.service.interfaces.UserSearchService;
import com.kyn.user.module.user.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final AuthenticationService authenticationService;
    private final UserSearchService userSearchService;
    public UserManagementServiceImpl(UserService userService, AuthorizationService authorizationService, UserSearchService userSearchService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.userSearchService = userSearchService;
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional
    public Mono<UserResponseDto> createUser(UserRequestDto dto) {
        var userInfoDto = UserManagementDtoMapper.userRequestDtoToUserInfoDto(dto);
        return userSearchService.isExistUser(userInfoDto.getEmail())
            .flatMap(isExist -> {
                if (isExist) return Mono.error(new AlreadySignedUserException());
                return userService.createUser(userInfoDto)
                    .flatMap(userInfo -> 
                    authorizationService.addUserAuth(UserManagementDtoMapper.userInfoDtoToUserAuthDto(userInfo, Role.USER))
                            .map(savedAuth -> UserInfoEntityDtoMapper
                            .userInfoEntityWithAuthDtoToUserResponseDto
                                (UserInfoEntityDtoMapper.dtoToEntity(userInfo), Collections.singletonList(savedAuth))));
            });
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

    @Override
    public Mono<Boolean> isValidUser(UserRequestDto dto){
        var userInfoDto = UserManagementDtoMapper.userRequestDtoToUserInfoDto(dto);
        return userService.isValidUser(userInfoDto);
    }

    @Override
    public Mono<TokenDto> login(UserRequestDto dto){
        var userInfoDto = UserManagementDtoMapper.userRequestDtoToUserInfoDto(dto);
        return authenticationService.login(userInfoDto);

    }
}

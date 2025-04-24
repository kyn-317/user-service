package com.kyn.user.module.management.mapper;

import java.util.Collections;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authorization.dto.UserAuthDto;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.management.dto.UserResponseDto;
import com.kyn.user.module.user.dto.UserInfoDto;

public class UserManagementDtoMapper {
    
    public static UserAuthDto userInfoDtoToUserAuthDto(UserInfoDto userInfoDto, Role role) {
        return UserAuthDto.builder()
                .userInfoId(userInfoDto.getUserInfoId())
                .email(userInfoDto.getEmail())
                .role(role)
                .build();
    }

    public static UserInfoDto userRequestDtoToUserInfoDto(UserRequestDto userRequestDto) {
        return UserInfoDto.builder()
                .userInfoId(userRequestDto.getUserInfoId())
                .userId(userRequestDto.getUserId())
                .userName(userRequestDto.getUserName())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .build();
    }

    public static UserResponseDto userInfoDtoToUserResponseDto(UserInfoDto userInfoDto) {
        return UserResponseDto.builder()
                .userId(userInfoDto.getUserId())
                .userName(userInfoDto.getUserName())
                .email(userInfoDto.getEmail())
                .build();
    }

    public static UserAuthDto userRequestDtoToUserAuthDto(UserRequestDto userRequestDto) {
        return UserAuthDto.builder()
                .userInfoId(userRequestDto.getUserInfoId())
                .email(userRequestDto.getEmail())
                .role(userRequestDto.getRole())
                .build();
    }
    
    public static UserResponseDto userAuthDtoToUserResponseDto(UserAuthDto userAuthDto) {
        return UserResponseDto.builder()
        .userAuths(Collections.singletonList(userAuthDto))
        .build();

    }

}

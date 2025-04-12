package com.kyn.user.module.mapper;

import java.util.Collections;

import org.springframework.beans.BeanUtils;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.dto.UserAuthDto;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;
import com.kyn.user.module.entity.UserInfoEntity;

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

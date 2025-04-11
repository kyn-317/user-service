package com.kyn.user.module.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.kyn.user.module.dto.UserAuthDto;
import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;
import com.kyn.user.module.entity.UserAuthEntity;
import com.kyn.user.module.entity.UserInfoEntity;

public class UserInfoEntityDtoMapper {
    

    public static UserInfoEntity userInfoEntityToCreateUser(UserRequestDto dto, String createdBy) {
        var userInfoEntity = UserInfoEntity.create(
            null,
            dto.getUserId(),
            dto.getUserName(),
            dto.getEmail(),
            dto.getPassword()
        );
        userInfoEntity.insertDocument(createdBy);
        return userInfoEntity;
    }

    public static UserResponseDto userInfoEntityToUserResponseDto(UserInfoEntity entity, List<UserAuthEntity> auths) {
        return UserResponseDto.builder()
        .userId(entity.getUserId())
        .userName(entity.getUserName())
        .email(entity.getEmail())
        .userAuths(auths.stream().map(UserAuthEntityDtoMapper::userAuthEntityToDto).collect(Collectors.toList()))
        .createdBy(entity.getCreatedBy())
        .createdAt(entity.getCreatedAt())
        .updatedBy(entity.getUpdatedBy())
        .updatedAt(entity.getUpdatedAt())
        .build();
    }

    public static UserResponseDto userInfoEntityWithAuthDtoToUserResponseDto(UserInfoEntity entity, List<UserAuthDto> authsDto) {
        return UserResponseDto.builder()
        .userId(entity.getUserId())
        .userName(entity.getUserName())
        .email(entity.getEmail())
        .userAuths(authsDto)
        .createdBy(entity.getCreatedBy())
        .createdAt(entity.getCreatedAt())
        .updatedBy(entity.getUpdatedBy())
        .updatedAt(entity.getUpdatedAt())
        .build();
    }
        
}

package com.kyn.user.module.user.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.kyn.user.module.authorization.dto.UserAuthDto;
import com.kyn.user.module.authentication.entity.UserAuthEntity;
import com.kyn.user.module.authentication.mapper.UserAuthEntityDtoMapper;
import com.kyn.user.module.management.dto.UserResponseDto;
import com.kyn.user.module.user.dto.UserInfoDto;
import com.kyn.user.module.user.entity.UserInfoEntity;

public class UserInfoEntityDtoMapper {
    

    public static UserInfoEntity userInfoEntityforCreateUser(UserInfoDto dto, String createdBy) {
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

    //dto to entity
    public static UserInfoEntity dtoToEntity(UserInfoDto dto) {
        UserInfoEntity entity = new UserInfoEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    //update user info when data is not null and not empty
    public static UserInfoEntity userInfoEntityforUpdateUser(UserInfoEntity entity, UserInfoDto dto, String updatedBy) {
        if(dto.getUserName() != null && !dto.getUserName().isEmpty()) 
            entity.setUserName(dto.getUserName());
        if(dto.getEmail() != null && !dto.getEmail().isEmpty()) 
            entity.setEmail(dto.getEmail());
        if(dto.getPassword() != null && !dto.getPassword().isEmpty()) 
            entity.setPassword(dto.getPassword());
        
        entity.updateDocument(updatedBy);
        return entity;
    }

    public static UserInfoDto EntityToDto(UserInfoEntity entity){
        return UserInfoDto.builder()
            .userInfoId(entity.getUserInfoId())
            .userId(entity.getUserId())
            .userName(entity.getUserName())
            .email(entity.getEmail())
            .createdBy(entity.getCreatedBy())
            .createdAt(entity.getCreatedAt())
            .updatedBy(entity.getUpdatedBy())
            .updatedAt(entity.getUpdatedAt())
            .build();
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
        .userInfoId(entity.getUserInfoId())
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

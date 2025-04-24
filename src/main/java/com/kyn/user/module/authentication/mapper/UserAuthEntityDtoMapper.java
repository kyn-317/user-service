package com.kyn.user.module.authentication.mapper;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authorization.dto.UserAuthDto;
import com.kyn.user.module.user.entity.UserInfoEntity;
import com.kyn.user.module.authentication.entity.UserAuthEntity;

public class UserAuthEntityDtoMapper {
    

    public static UserAuthEntity userAuthEntityToCreateUser(UserInfoEntity userInfoEntity, Role role) {
        UserAuthEntity authEntity = UserAuthEntity.create(
            null,
            userInfoEntity.getUserInfoId(),
            userInfoEntity.getEmail(),
            role
        );
        authEntity.insertDocument(userInfoEntity.getUserId());
        return authEntity;
    }


    public static UserAuthEntity userAuthEntityToCreate(UserAuthDto dto, String createdBy) {
        UserAuthEntity authEntity = UserAuthEntity.create(
            null,
            dto.getUserInfoId(),
            dto.getEmail(),
            dto.getRole()
        );
        authEntity.insertDocument(createdBy);
        return authEntity;
    }
    public static UserAuthDto userAuthEntityToDto(UserAuthEntity authEntity) {
        return UserAuthDto.create(
            authEntity.getUserAuthId(),
            authEntity.getUserInfoId(),
            authEntity.getEmail(),
            authEntity.getRole(),
            authEntity.getCreatedBy(),
            authEntity.getCreatedAt(),
            authEntity.getUpdatedBy(),
            authEntity.getUpdatedAt()
        );
    }
    
    public static UserAuthEntity userAuthDtoToEntity(UserAuthDto dto) {
        UserAuthEntity authEntity = UserAuthEntity.create(
            dto.getUserAuthId(),
            dto.getUserInfoId(),
            dto.getEmail(),
            dto.getRole());
        authEntity.setCreatedBy(dto.getCreatedBy());
        authEntity.setCreatedAt(dto.getCreatedAt());
        authEntity.setUpdatedBy(dto.getUpdatedBy());
        authEntity.setUpdatedAt(dto.getUpdatedAt());
        return authEntity;
    }
}

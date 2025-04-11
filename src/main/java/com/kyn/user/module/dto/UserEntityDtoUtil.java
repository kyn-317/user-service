package com.kyn.user.module.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.entity.UserAuthEntity;
import com.kyn.user.module.entity.UserInfoEntity;

public class UserEntityDtoUtil {

    // UserInfoEntity, userASuthEntity -> UserInfoDto 
    public static UserInfoDto entityToDto(UserInfoEntity entity, List<UserAuthEntity> auths) {
        UserInfoDto dto = new UserInfoDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.get_id());

        if (auths != null && !auths.isEmpty()) {
            List<UserAuthDto> authDtos = auths.stream()
                    .map(UserEntityDtoUtil::authEntityToDto)
                    .collect(Collectors.toList());
            dto.setUserAuths(authDtos);
        }

        return dto;
    }

    public static UserInfoEntity createUserInfoEntity(UserInfoDto dto) {
        UserInfoEntity entity = UserEntityDtoUtil.dtoToEntity(dto);
        entity.insertDocument(dto.getUserId());
        return entity;
    }

    public static UserInfoEntity updateUserInfoEntity(UserInfoDto dto, UserInfoEntity existingUser,
            PasswordEncoder encoder) {
        // password update
        if (dto.getUserPassword() != null
                && !dto.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(
                    encoder.encode(dto.getUserPassword()));
        }
        // name update
        if (dto.getUserName() != null
                && !dto.getUserName().isEmpty()) {
            existingUser.setUserName(dto.getUserName());
        }
        // email update
        if (dto.getUserEmail() != null
                && !dto.getUserEmail().isEmpty()) {
            existingUser.setUserEmail(dto.getUserEmail());
        }
        // update Document
        existingUser.updateDocument(existingUser.getUserId());
        return existingUser;
    }

    public static UserAuthEntity createUserAuthEntity(UserInfoEntity infoEntity) {
        UserAuthEntity authEntity = UserAuthEntity.create(null, infoEntity.get_id(),
                infoEntity.getUserEmail(), Role.USER);
        authEntity.insertDocument(infoEntity.getUserId());
        return authEntity;

    }

    public static UserInfoEntity dtoToEntity(UserInfoDto dto) {
        UserInfoEntity entity = new UserInfoEntity();
        BeanUtils.copyProperties(dto, entity);

        if (dto.getId() != null) {
            entity.set_id(dto.getId());
        }

        return entity;
    }

    // UserAuthEntity <-> UserAuthDto 변환
    public static UserAuthDto authEntityToDto(UserAuthEntity entity) {
        UserAuthDto dto = new UserAuthDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.get_id());
        dto.setUserObjectId(entity.getUserObjectId());

        return dto;
    }

    public static UserAuthEntity authDtoToEntity(UserAuthDto dto) {
        UserAuthEntity entity = new UserAuthEntity();
        BeanUtils.copyProperties(dto, entity);

        if (dto.getId() != null) {
            entity.set_id(dto.getId());
        }

        if (dto.getUserObjectId() != null) {
            entity.setUserObjectId(dto.getUserObjectId());
        }

        return entity;
    }

}
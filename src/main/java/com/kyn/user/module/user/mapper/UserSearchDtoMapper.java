package com.kyn.user.module.user.mapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authentication.dto.AuthenticationUserDto;
import com.kyn.user.module.authorization.dto.UserAuthDto;
import com.kyn.user.module.management.dto.UserResponseDto;
import com.kyn.user.module.management.dto.UserSearchDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserSearchDtoMapper {
    //setUserAuthDto
    private static final Function<UserSearchDto, UserAuthDto> toUserAuthDto = d -> {
        UserAuthDto authDto = new UserAuthDto();
        authDto.setUserAuthId(d.getUserAuthId());
        authDto.setUserInfoId(d.getUserInfoId());
        authDto.setEmail(d.getEmail());
        authDto.setRole(Role.valueOf(d.getRole()));
        authDto.setCreatedBy(d.getUaCreatedBy());
        authDto.setCreatedAt(d.getUaCreatedAt());
        authDto.setUpdatedBy(d.getUaUpdatedBy());
        authDto.setUpdatedAt(d.getUaUpdatedAt());
        return authDto;
    };

    //setUserResponseDto
    public static final Function<List<UserSearchDto>, UserResponseDto> toUserResponseDto = searchDtos -> {
        if (searchDtos.isEmpty()) {
            return null;
        }
                
        List<UserSearchDto> userDtos =  searchDtos.stream()
        .collect(Collectors.groupingBy(UserSearchDto::getUserInfoId)).values().iterator().next();
        UserSearchDto firstDto = userDtos.get(0);
        
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUserInfoId(firstDto.getUserInfoId());
        responseDto.setUserId(firstDto.getUserId());
        responseDto.setUserName(firstDto.getUserName());
        responseDto.setEmail(firstDto.getEmail());
        responseDto.setCreatedBy(firstDto.getCreatedBy());
        responseDto.setCreatedAt(firstDto.getCreatedAt());
        responseDto.setUpdatedBy(firstDto.getUpdatedBy());
        responseDto.setUpdatedAt(firstDto.getUpdatedAt());
        
        List<UserAuthDto> authDtos = userDtos.stream()
            .filter(d -> d.getUserAuthId() != null)
            .map(toUserAuthDto)
            .collect(Collectors.toList());
        
        responseDto.setUserAuths(authDtos);
        return responseDto;
    };

    public static final Function<List<UserSearchDto>, AuthenticationUserDto> toAuthenticationUserDto = searchDtos -> {
        if (searchDtos.isEmpty()) {
            return null;
        }
        
        Map<UUID, List<UserSearchDto>> groupedByUserInfoId = searchDtos.stream()
            .collect(Collectors.groupingBy(UserSearchDto::getUserInfoId));
        
        List<UserSearchDto> userDtos = groupedByUserInfoId.values().iterator().next();
        UserSearchDto firstDto = userDtos.get(0);
        
        AuthenticationUserDto responseDto = new AuthenticationUserDto();
        responseDto.setUserInfoId(firstDto.getUserInfoId());
        responseDto.setUserId(firstDto.getUserId());
        responseDto.setUserName(firstDto.getUserName());
        responseDto.setEmail(firstDto.getEmail());
        responseDto.setPassword(firstDto.getPassword());
        
        List<UserAuthDto> authDtos = userDtos.stream()
            .filter(d -> d.getUserAuthId() != null)
            .map(toUserAuthDto)
            .collect(Collectors.toList());
        
        responseDto.setUserAuths(authDtos);
        return responseDto;
    };

}

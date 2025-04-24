package com.kyn.user.module.authentication.dto;

import java.util.List;
import java.util.UUID;

import com.kyn.user.module.authorization.dto.UserAuthDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
public class AuthenticationUserDto {
    private UUID userInfoId;
    private String userId;
    private String email;
    private String userName;
    private String password;
    private List<UserAuthDto> userAuths;
    
}

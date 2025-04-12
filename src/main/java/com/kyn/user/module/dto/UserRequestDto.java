package com.kyn.user.module.dto;

import java.util.UUID;

import com.kyn.user.base.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
public class UserRequestDto {
    private UUID userInfoId;
    private String userId;
    private String userName;
    private String email;
    private String password;
    private Role role;
}

package com.kyn.user.module.authorization.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kyn.user.base.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
public class UserAuthDto {
    private UUID userAuthId;
    private UUID userInfoId;
    private String email;
    private Role role;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
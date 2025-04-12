package com.kyn.user.module.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private UUID userInfoId;
    private String userId;
    private String email;
    private String userName;
    private String password;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    private UUID userAuthId;
    private String role;
    private String uaCreatedBy;
    private LocalDateTime uaCreatedAt;
    private String uaUpdatedBy;
    private LocalDateTime uaUpdatedAt;
}

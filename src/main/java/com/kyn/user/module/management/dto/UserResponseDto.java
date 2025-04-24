package com.kyn.user.module.management.dto;

import java.time.LocalDateTime;
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
public class UserResponseDto {
    private UUID userInfoId;
    private String userId;
    private String email;
    private String userName;
    private List<UserAuthDto> userAuths;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}

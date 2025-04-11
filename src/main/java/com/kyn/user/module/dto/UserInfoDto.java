package com.kyn.user.module.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class UserInfoDto {
    private UUID userInfoId;
    private String userId;
    private String userName;
    private String email;
    private String password;
    private List<UserAuthDto> userAuths;

    private String regrId;
    private LocalDateTime regDt;
    private String amdrId;
    private LocalDateTime amdDt;
}
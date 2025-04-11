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
    private UUID id;
    private String userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private List<UserAuthDto> userAuths;

    private String regrId;
    private LocalDateTime regDt;
    private String amdrId;
    private LocalDateTime amdDt;
}
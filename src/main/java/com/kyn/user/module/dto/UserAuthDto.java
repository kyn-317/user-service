package com.kyn.user.module.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kyn.user.base.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class UserAuthDto {
    private UUID id;
    private UUID userObjectId;
    private String userEmail;
    private Role userRole;

    private String regrId;
    private LocalDateTime regDt;
    private String amdrId;
    private LocalDateTime amdDt;
}
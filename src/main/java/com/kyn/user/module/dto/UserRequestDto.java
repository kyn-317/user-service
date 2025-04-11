package com.kyn.user.module.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Builder
public class UserRequestDto {
    private String userId;
    private String userName;
    private String email;
    private String password;
}

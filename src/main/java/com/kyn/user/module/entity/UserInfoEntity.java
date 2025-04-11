package com.kyn.user.module.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import org.springframework.data.relational.core.mapping.Table;

import com.kyn.user.base.entity.BaseDocuments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@Table(schema = "user_data", name = "user_info")
public class UserInfoEntity extends BaseDocuments {
    @Id
    private UUID userInfoId;

    private String userId;
    
    private String userName;

    private String email;

    private String password;

}

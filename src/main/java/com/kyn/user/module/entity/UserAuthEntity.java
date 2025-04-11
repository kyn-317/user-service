package com.kyn.user.module.entity;

import java.util.UUID;
    
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.kyn.user.base.entity.BaseDocuments;
import com.kyn.user.base.enums.Role;

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
@Table(schema = "user", name = "user_auth")
public class UserAuthEntity extends BaseDocuments {

    @Id
    private UUID _id;

    private UUID userObjectId;

    private String userEmail;

    private Role userRole;

}

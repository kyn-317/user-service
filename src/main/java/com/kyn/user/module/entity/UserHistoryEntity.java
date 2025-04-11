package com.kyn.user.module.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;

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
public class UserHistoryEntity extends BaseDocuments {

    @Id
    private UUID _id;

}

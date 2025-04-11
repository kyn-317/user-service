package com.kyn.user.base.entity;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
public class BaseDocuments {

    @Column("created_by")
    private String createdBy;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private LocalDateTime updatedAt;
    
    
    public void insertDocument(String userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDocument(String userId) {
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }
}

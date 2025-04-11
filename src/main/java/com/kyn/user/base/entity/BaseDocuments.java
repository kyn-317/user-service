package com.kyn.user.base.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaseDocuments {

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

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

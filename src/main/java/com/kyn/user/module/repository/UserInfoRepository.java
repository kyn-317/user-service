package com.kyn.user.module.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.entity.UserInfoEntity;

import reactor.core.publisher.Mono;

@Repository
public interface UserInfoRepository extends ReactiveCrudRepository<UserInfoEntity, UUID> {
    Mono<UserInfoEntity> findByEmail(String email);

    Mono<UserInfoEntity> findByUserId(String userId);

    @Query("""
        SELECT ui.*, ua.user_auth_id, ua.user_info_id, ua.email, ua.role, 
               ua.created_by, ua.created_at, ua.updated_by, ua.updated_at
        FROM user_data.user_info ui
        LEFT JOIN user_data.user_auth ua ON ui.user_info_id = ua.user_info_id
        WHERE ui.user_id = :userId
    """)
    Mono<UserInfoEntity> findByUserIdWithAuth(String userId);
}

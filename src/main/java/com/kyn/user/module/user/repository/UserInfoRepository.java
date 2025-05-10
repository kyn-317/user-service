package com.kyn.user.module.user.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.user.module.management.dto.UserSearchDto;
import com.kyn.user.module.user.entity.UserInfoEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserInfoRepository extends ReactiveCrudRepository<UserInfoEntity, UUID> {
    Mono<UserInfoEntity> findByEmail(String email);

    Mono<UserInfoEntity> findByUserId(String userId);

    Mono<Boolean> existsByEmailAndPassword(String email, String password);

    @Query("""
           SELECT 
               ui.user_info_id AS user_info_id, ui.user_id AS user_id, ui.user_name AS user_name, ui.email AS email, 
               ui.created_by AS created_by, ui.created_at AS created_at, ui.updated_by AS updated_by, ui.updated_at AS updated_at, 
               ua.user_auth_id AS user_auth_id, ua.role AS role, ua.created_by AS ua_created_by , ua.created_at AS ua_created_at , 
               ua.updated_by AS ua_updated_by, ua.updated_at AS ua_updated_at
           FROM 
               user_data.user_info ui LEFT JOIN user_data.user_auth ua ON ui.user_info_id = ua.user_info_id
           WHERE 1=1
               AND CASE 
                   WHEN :userId IS NOT NULL THEN ui.user_id = :userId
                   ELSE true
               END
               AND CASE 
                   WHEN :userInfoId IS NOT NULL THEN ui.user_info_id = :userInfoId
                   ELSE true
               END
               AND CASE 
                   WHEN :email IS NOT NULL THEN ui.email = :email
                   ELSE true
               END
               AND CASE 
                   WHEN :userName IS NOT NULL THEN ui.user_name = :userName
                   ELSE true
               END
           """)
    Flux<UserSearchDto> findByUserIdWithAuth(
            @Param("userId") String userId,
            @Param("userInfoId") UUID userInfoId,
            @Param("email") String email,
            @Param("userName") String userName
    );

    @Query("""
        SELECT 
            ui.user_info_id AS user_info_id, ui.user_id AS user_id, ui.user_name AS user_name, ui.email AS email, ui.password AS password,
            ua.user_auth_id AS user_auth_id, ua.role AS role
        FROM 
            user_data.user_info ui LEFT JOIN user_data.user_auth ua ON ui.user_info_id = ua.user_info_id
        WHERE 1=1
            AND CASE 
                WHEN :userId IS NOT NULL THEN ui.user_id = :userId
                ELSE true
            END
            AND CASE 
                WHEN :userInfoId IS NOT NULL THEN ui.user_info_id = :userInfoId
                ELSE true
            END
            AND CASE 
                WHEN :email IS NOT NULL THEN ui.email = :email
                ELSE true
            END
            AND CASE 
                WHEN :userName IS NOT NULL THEN ui.user_name = :userName
                ELSE true
            END
        """)
    Flux<UserSearchDto> findByUserIdWithAuthGetPassword(
             @Param("userId") String userId,
             @Param("userInfoId") UUID userInfoId,
             @Param("email") String email,
             @Param("userName") String userName
    );

    Mono<Boolean> existsByEmail(String email);

}

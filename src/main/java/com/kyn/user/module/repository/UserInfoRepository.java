package com.kyn.user.module.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.user.module.entity.UserInfoEntity;

import reactor.core.publisher.Mono;

@Repository
public interface UserInfoRepository extends ReactiveCrudRepository<UserInfoEntity, UUID> {
    Mono<UserInfoEntity> findByUserEmail(String userEmail);

    Mono<UserInfoEntity> findByUserId(String userId);
}

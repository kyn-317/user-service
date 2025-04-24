package com.kyn.user.module.authentication.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authentication.entity.UserAuthEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserAuthRepository extends ReactiveCrudRepository<UserAuthEntity, UUID> {
    Flux<UserAuthEntity> findByUserInfoId(UUID userInfoId);

    Mono<UserAuthEntity> findByUserInfoIdAndRole(UUID userInfoId, Role role);

    Mono<Void> deleteByUserInfoIdAndRole(UUID userInfoId, Role role);
}

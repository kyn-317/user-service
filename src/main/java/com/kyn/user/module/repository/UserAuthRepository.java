package com.kyn.user.module.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.entity.UserAuthEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserAuthRepository extends ReactiveCrudRepository<UserAuthEntity, UUID> {
    Flux<UserAuthEntity> findByUserObjectId(UUID userObjectId);

    Mono<UserAuthEntity> findByUserObjectIdAndUserRole(UUID userObjectId, Role userRole);

    Mono<Void> deleteByUserObjectIdAndUserRole(UUID userObjectId, Role userRole);
}

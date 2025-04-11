package com.kyn.user.module.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.dto.UserAuthDto;
import com.kyn.user.module.dto.UserEntityDtoUtil;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.entity.UserAuthEntity;
import com.kyn.user.module.repository.UserAuthRepository;
import com.kyn.user.module.repository.UserInfoRepository;
import com.kyn.user.module.service.interfaces.AuthorizationService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserInfoRepository userInfoRepository;
    private final UserAuthRepository userAuthRepository;

    public AuthorizationServiceImpl(UserInfoRepository userInfoRepository, UserAuthRepository userAuthRepository) {
            this.userInfoRepository = userInfoRepository;
            this.userAuthRepository = userAuthRepository;
    }

    @Override
    public Mono<UserInfoDto> addUserAuth(UUID userId, UserAuthDto userAuthDto) {
        return userInfoRepository.findById(userId)
        .flatMap(user -> {
                return userAuthRepository
                                .findByUserObjectIdAndUserRole(user.get_id(),
                                                userAuthDto.getUserRole())
                                .flatMap(existingAuth -> {
                                        return userAuthRepository
                                                        .findByUserObjectId(user.get_id())
                                                        .collectList()
                                                        .map(auths -> UserEntityDtoUtil
                                                                        .entityToDto(
                                                                                        user,
                                                                                        auths));
                                })
                                .switchIfEmpty(
                                                Mono.defer(() -> {
                                                        UserAuthEntity authEntity = new UserAuthEntity();
                                                        authEntity.setUserObjectId(
                                                                        user.get_id());
                                                        authEntity.setUserEmail(
                                                                        user.getUserEmail());
                                                        authEntity.setUserRole(userAuthDto
                                                                        .getUserRole());
                                                        authEntity.insertDocument(
                                                                        user.getUserId());

                                                        return userAuthRepository
                                                                        .save(authEntity)
                                                                        .then(userAuthRepository
                                                                                        .findByUserObjectId(
                                                                                                        user.get_id())
                                                                                        .collectList()
                                                                                        .map(auths -> UserEntityDtoUtil
                                                                                                        .entityToDto(user,
                                                                                                                        auths)));
                                                }));
        });
    }

    @Override
    public Mono<UserInfoDto> removeUserAuth(UUID userId, Role role) {
        return userInfoRepository.findById(userId)
        .flatMap(user -> {

                return userAuthRepository.findByUserObjectId(user.get_id())
                                .collectList()
                                .flatMap(auths -> {

                                        if (auths.size() <= 1) {
                                                return Mono.just(UserEntityDtoUtil.entityToDto(
                                                                user,
                                                                auths));
                                        }

                                        return userAuthRepository
                                                        .deleteByUserObjectIdAndUserRole(
                                                                        user.get_id(), role)
                                                        .then(userAuthRepository
                                                                        .findByUserObjectId(user
                                                                                        .get_id())
                                                                        .collectList()
                                                                        .map(updatedAuths -> UserEntityDtoUtil
                                                                                        .entityToDto(user,
                                                                                                        updatedAuths)));
                                });
        });
    }

    @Override
    public Flux<UserAuthDto> getUserAuths(UUID userId) {
        return userInfoRepository.findById(userId)
        .flatMapMany(user -> userAuthRepository.findByUserObjectId(user.get_id())
                        .map(UserEntityDtoUtil::authEntityToDto));
    }
    
}

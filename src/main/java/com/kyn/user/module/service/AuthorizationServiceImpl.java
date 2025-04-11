package com.kyn.user.module.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.dto.UserAuthDto;
import com.kyn.user.module.dto.UserEntityDtoUtil;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.entity.UserAuthEntity;
import com.kyn.user.module.entity.UserInfoEntity;
import com.kyn.user.module.mapper.UserAuthEntityDtoMapper;
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
    public Mono<UserAuthDto> addUserAuth(UUID userId, Role role) {
        return userInfoRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user ->  userAuthRepository.findByUserInfoIdAndRole(user.getUserInfoId(), role)
                            .map(UserAuthEntityDtoMapper::userAuthEntityToDto)
                            .switchIfEmpty(Mono.defer(() -> 
                                        userAuthRepository.save(UserAuthEntityDtoMapper.userAuthEntityToCreateUser(user, role))
                                        .map(UserAuthEntityDtoMapper::userAuthEntityToDto))));
    }

    @Override
    public Mono<UserInfoDto> removeUserAuth(UUID userId, Role role) {
        return userInfoRepository.findById(userId)
        .flatMap(user -> {

                return userAuthRepository.findByUserInfoId(user.getUserInfoId())
                                .collectList()
                                .flatMap(auths -> {

                                        if (auths.size() <= 1) {
                                                return Mono.just(UserEntityDtoUtil.entityToDto(
                                                                user,
                                                                auths));
                                        }

                                        return userAuthRepository
                                                        .deleteByUserInfoIdAndRole(
                                                                        user.getUserInfoId(), role)
                                                        .then(userAuthRepository
                                                                        .findByUserInfoId(user
                                                                                        .getUserInfoId())
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
        .flatMapMany(user -> userAuthRepository.findByUserInfoId(user.getUserInfoId())
                        .map(UserEntityDtoUtil::authEntityToDto));
    }
    
}

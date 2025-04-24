package com.kyn.user.module.authorization.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authentication.mapper.UserAuthEntityDtoMapper;
import com.kyn.user.module.authentication.repository.UserAuthRepository;
import com.kyn.user.module.authorization.dto.UserAuthDto;
import com.kyn.user.module.authorization.service.interfaces.AuthorizationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserAuthRepository userAuthRepository;


    //add user auth
    @Override
    public Mono<UserAuthDto> addUserAuth(UserAuthDto dto) {
        return userAuthRepository.findByUserInfoIdAndRole(dto.getUserInfoId(), dto.getRole())
                            .switchIfEmpty(Mono.defer(() -> userAuthRepository.
                            save(UserAuthEntityDtoMapper.userAuthEntityToCreate(dto, "system"))))
                                        .map(UserAuthEntityDtoMapper::userAuthEntityToDto);
    }
    // remove user auth
    @Override
    public Mono<Void> removeUserAuth(UUID userId, Role role) {
        return userAuthRepository.deleteByUserInfoIdAndRole(userId, role).then();
    }

    // get user auths
    @Override
    public Flux<UserAuthDto> getUserAuths(UUID userId) {
        return userAuthRepository.findByUserInfoId(userId)
                        .map(UserAuthEntityDtoMapper::userAuthEntityToDto);
    }


}

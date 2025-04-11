package com.kyn.user.module.service;

import java.util.Collections;
import java.util.UUID;
import java.util.List;

import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyn.user.base.enums.Role;
import com.kyn.user.base.security.JwtTokenProvider;
import com.kyn.user.module.dto.UserEntityDtoUtil;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.dto.UserRequestDto;
import com.kyn.user.module.dto.UserResponseDto;
import com.kyn.user.module.entity.UserAuthEntity;
import com.kyn.user.module.mapper.UserInfoEntityDtoMapper;
import com.kyn.user.module.repository.UserAuthRepository;
import com.kyn.user.module.repository.UserInfoRepository;
import com.kyn.user.module.service.interfaces.AuthorizationService;
import com.kyn.user.module.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

        private final UserInfoRepository userInfoRepository;
        private final UserAuthRepository userAuthRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthorizationService authorizationService;

        public UserServiceImpl(UserInfoRepository userInfoRepository, UserAuthRepository userAuthRepository,
                        PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                        RedissonClient redissonClient, AuthorizationService authorizationService) {
                this.userInfoRepository = userInfoRepository;
                this.userAuthRepository = userAuthRepository;
                this.passwordEncoder = passwordEncoder;
                this.authorizationService = authorizationService;
        }

        @Override
        public Mono<UserResponseDto> createUser(UserRequestDto userRequestDto) {
                // password encode and entity set
                userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
                // create userInfoEntity -> saveUserAuthEntity by savedUserInfo -> make UserInfoDto and return 
                return userInfoRepository.save(UserInfoEntityDtoMapper.userInfoEntityToCreateUser(userRequestDto,"system"))
                                .flatMap(savedUser -> authorizationService.addUserAuth(savedUser.getUserInfoId(), Role.USER)
                                        .map(savedAuth -> UserInfoEntityDtoMapper
                                        .userInfoEntityWithAuthDtoToUserResponseDto(savedUser, Collections.singletonList(savedAuth))));
        }

        // update UserInfo (email, password, name only can update)
        @Override
        public Mono<UserInfoDto> updateUser(UserInfoDto userInfoDto) {
                        return userInfoRepository.findById(userInfoDto.getUserInfoId())
                                        .flatMap(existingUser -> {
                                                return userInfoRepository
                                                                .save(UserEntityDtoUtil.updateUserInfoEntity(userInfoDto,
                                                                                existingUser, passwordEncoder))
                                                                .flatMap(updatedUser -> userAuthRepository
                                                                                .findByUserInfoId(updatedUser.getUserInfoId())
                                                                                .collectList()
                                                                                .map(auths -> UserEntityDtoUtil.entityToDto(
                                                                                                updatedUser, auths)));
                                        });
                }

        // search UserInfo
        @Override
        public Mono<UserInfoDto> findUserById(UUID id) {
                return userInfoRepository.findById(id)
                                .flatMap(user -> userAuthRepository.findByUserInfoId(user.getUserInfoId())
                                                .collectList()
                                                .map(auths -> UserEntityDtoUtil.entityToDto(user, auths)));
        }

        // search UserInfo by email
        @Override
        public Mono<UserInfoDto> findUserByEmail(String email) {
                return userInfoRepository.findByEmail(email)
                                .flatMap(user -> userAuthRepository.findByUserInfoId(user.getUserInfoId())
                                                .collectList()
                                                .map(auths -> UserEntityDtoUtil.entityToDto(user, auths)));
        }

        // search UserInfo by userId
        @Override
        public Mono<UserInfoDto> findUserByUserId(String userId) {
                return userInfoRepository.findByUserId(userId)
                                .flatMap(user -> userAuthRepository.findByUserInfoId(user.getUserInfoId())
                                                .collectList()
                                                .map(auths -> UserEntityDtoUtil.entityToDto(user, auths)));
        }

}
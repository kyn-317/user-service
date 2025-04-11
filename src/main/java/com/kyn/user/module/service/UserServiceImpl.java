package com.kyn.user.module.service;

import java.util.Collections;
import java.util.UUID;

import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyn.user.base.security.JwtTokenProvider;
import com.kyn.user.module.dto.UserEntityDtoUtil;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.repository.UserAuthRepository;
import com.kyn.user.module.repository.UserInfoRepository;
import com.kyn.user.module.service.interfaces.IUserService;

import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements IUserService {

        private final UserInfoRepository userInfoRepository;
        private final UserAuthRepository userAuthRepository;
        private final PasswordEncoder passwordEncoder;

        public UserServiceImpl(UserInfoRepository userInfoRepository, UserAuthRepository userAuthRepository,
                        PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                        RedissonClient redissonClient) {
                this.userInfoRepository = userInfoRepository;
                this.userAuthRepository = userAuthRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public Mono<UserInfoDto> createUser(UserInfoDto userInfoDto) {
                // password encode and entity set
                userInfoDto.setUserPassword(passwordEncoder.encode(userInfoDto.getUserPassword()));
                // create userInfoEntity -> saveUserAuthEntity by savedUserInfo -> make UserInfoDto and return 
                return userInfoRepository.save(UserEntityDtoUtil.createUserInfoEntity(userInfoDto))
                                .flatMap(savedUser -> userAuthRepository
                                                .save(UserEntityDtoUtil.createUserAuthEntity(savedUser))
                                                .map(savedAuth -> UserEntityDtoUtil.entityToDto(
                                                                savedUser,
                                                                Collections.singletonList(savedAuth))));
        }

        // update UserInfo (email, password, name only can update)
        @Override
        public Mono<UserInfoDto> updateUser(UserInfoDto userInfoDto) {
                        return userInfoRepository.findById(userInfoDto.getId())
                                        .flatMap(existingUser -> {
                                                return userInfoRepository
                                                                .save(UserEntityDtoUtil.updateUserInfoEntity(userInfoDto,
                                                                                existingUser, passwordEncoder))
                                                                .flatMap(updatedUser -> userAuthRepository
                                                                                .findByUserObjectId(updatedUser.get_id())
                                                                                .collectList()
                                                                                .map(auths -> UserEntityDtoUtil.entityToDto(
                                                                                                updatedUser, auths)));
                                        });
                }

        // search UserInfo
        @Override
        public Mono<UserInfoDto> findUserById(UUID id) {
                return userInfoRepository.findById(id)
                                .flatMap(user -> userAuthRepository.findByUserObjectId(user.get_id())
                                                .collectList()
                                                .map(auths -> UserEntityDtoUtil.entityToDto(user, auths)));
        }

        // search UserInfo by email
        @Override
        public Mono<UserInfoDto> findUserByEmail(String email) {
                return userInfoRepository.findByUserEmail(email)
                                .flatMap(user -> userAuthRepository.findByUserObjectId(user.get_id())
                                                .collectList()
                                                .map(auths -> UserEntityDtoUtil.entityToDto(user, auths)));
        }

        // search UserInfo by userId
        @Override
        public Mono<UserInfoDto> findUserByUserId(String userId) {
                return userInfoRepository.findByUserId(userId)
                                .flatMap(user -> userAuthRepository.findByUserObjectId(user.get_id())
                                                .collectList()
                                                .map(auths -> UserEntityDtoUtil.entityToDto(user, auths)));
        }

}
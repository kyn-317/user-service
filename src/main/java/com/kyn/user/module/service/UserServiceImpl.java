package com.kyn.user.module.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyn.user.module.dto.UserEntityDtoUtil;
import com.kyn.user.module.dto.UserInfoDto;
import com.kyn.user.module.mapper.UserInfoEntityDtoMapper;
import com.kyn.user.module.repository.UserAuthRepository;
import com.kyn.user.module.repository.UserInfoRepository;
import com.kyn.user.module.service.interfaces.UserService;

import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

        private final UserInfoRepository userInfoRepository;
        private final UserAuthRepository userAuthRepository;
        private final PasswordEncoder passwordEncoder;

        public UserServiceImpl(UserInfoRepository userInfoRepository, UserAuthRepository userAuthRepository,
                        PasswordEncoder passwordEncoder) {
                this.userInfoRepository = userInfoRepository;
                this.userAuthRepository = userAuthRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public Mono<UserInfoDto> createUser(UserInfoDto userInfoDto) {
                userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
                return userInfoRepository.save(UserInfoEntityDtoMapper.userInfoEntityforCreateUser(userInfoDto,"system"))
                        .map(UserInfoEntityDtoMapper::EntityToDto);
        }

        // update UserInfo (email, password, name only can update)
        @Override
        public Mono<UserInfoDto> updateUser(UserInfoDto userInfoDto) {
                        return userInfoRepository.findById(userInfoDto.getUserInfoId())
                                .flatMap(user -> userInfoRepository.save(
                                        UserInfoEntityDtoMapper.userInfoEntityforUpdateUser
                                                                (user, userInfoDto, "system")))
                                .map(UserInfoEntityDtoMapper::EntityToDto);
        }


}
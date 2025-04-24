package com.kyn.user.module.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authentication.repository.UserAuthRepository;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.user.dto.UserInfoDto;
import com.kyn.user.module.user.repository.UserInfoRepository;
import com.kyn.user.module.user.service.interfaces.UserSearchService;
import com.kyn.user.module.user.service.interfaces.UserService;

import reactor.test.StepVerifier;


@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    @Autowired
    private UserSearchService userSearchService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;

    @BeforeEach
    void setUp() {
        userAuthRepository.deleteAll().subscribe();
        userInfoRepository.deleteAll().subscribe();
    }

    @Test
    void testCreateUser() {
        UserRequestDto userRequest = UserRequestDto.builder()
            .userId("testuser")
            .userName("Test User")
            .email("test@example.com")
            .password("password123")
            .role(Role.USER)
            .build();

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .userId(userRequest.getUserId())
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        StepVerifier.create(userService.createUser(userInfoDto))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void testFindUserByUserId() {
        UserRequestDto userRequest = UserRequestDto.builder()
            .userId("testuser")
            .userName("Test User")
            .email("test@example.com")
            .password("password123")
            .role(Role.USER)
            .build();

            UserInfoDto userInfoDto = UserInfoDto.builder()
            .userId(userRequest.getUserId())
            .userName(userRequest.getUserName())
            .email(userRequest.getEmail())
            .password(userRequest.getPassword())
            .build();

        StepVerifier.create(userService.createUser(userInfoDto))
            .expectNextCount(1)
            .verifyComplete();

        StepVerifier.create(userSearchService.findUserByUserId(userInfoDto.getUserId()))
            .assertNext( user -> {
                Assertions.assertEquals(user.getUserId(), userInfoDto.getUserId());
                Assertions.assertEquals(user.getUserName(), userInfoDto.getUserName());
                Assertions.assertEquals(user.getEmail(), userInfoDto.getEmail());
            })
            .verifyComplete();
    }
}

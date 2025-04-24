package com.kyn.user.module.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.user.dto.UserInfoDto;
import com.kyn.user.module.user.repository.UserInfoRepository;
import com.kyn.user.module.user.service.interfaces.UserService;

import reactor.test.StepVerifier;


@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void testCreateUser() {
        userInfoRepository.deleteAll().subscribe();
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
    
}

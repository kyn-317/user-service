package com.kyn.user.module.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kyn.user.module.authentication.service.interfaces.AuthenticationService;

@SpringBootTest
@ActiveProfiles("test")
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void contextLoads() {
        // 컨텍스트 로드 테스트
    }
} 
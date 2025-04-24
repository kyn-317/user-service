package com.kyn.user.module.management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kyn.user.module.management.service.interfaces.UserManagementService;

@SpringBootTest
@ActiveProfiles("test")
public class UserManagementServiceTest {

    @Autowired
    private UserManagementService userManagementService;

    @Test
    void contextLoads() {
        // 컨텍스트 로드 테스트
    }
} 
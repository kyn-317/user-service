package com.kyn.user.module.authorization;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authorization.dto.UserAuthDto;
import com.kyn.user.module.authorization.service.interfaces.AuthorizationService;

import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
public class AuthorizationServiceTest {

    @Autowired
    private AuthorizationService authorizationService;

    @Test
    void addUserAuthTest() {
        // 테스트용 UserAuthDto 생성
        UUID userId = UUID.randomUUID();
        UserAuthDto dto = UserAuthDto.builder()
                .userInfoId(userId)
                .role(Role.USER)
                .build();

        StepVerifier.create(authorizationService.addUserAuth(dto))
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(userId, result.getUserInfoId());
                    assertEquals(Role.USER, result.getRole());
                })
                .verifyComplete();
    }

    @Test
    void getUserAuthsTest() {
        UUID userId = UUID.randomUUID();
        UserAuthDto dto = UserAuthDto.builder()
                .userInfoId(userId)
                .role(Role.USER)
                .build();

        // 먼저 권한을 추가하고
        authorizationService.addUserAuth(dto).block();

        // 권한 조회 테스트
        StepVerifier.create(authorizationService.getUserAuths(userId))
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(userId, result.getUserInfoId());
                    assertEquals(Role.USER, result.getRole());
                })
                .verifyComplete();
    }

    @Test
    void removeUserAuthTest() {
        UUID userId = UUID.randomUUID();
        UserAuthDto dto = UserAuthDto.builder()
                .userInfoId(userId)
                .role(Role.USER)
                .build();

        // 먼저 권한을 추가
        authorizationService.addUserAuth(dto).block();

        // 권한 제거 테스트
        StepVerifier.create(authorizationService.removeUserAuth(userId, Role.USER))
                .verifyComplete();

        // 제거 확인
        StepVerifier.create(authorizationService.getUserAuths(userId))
                .verifyComplete();
    }
} 
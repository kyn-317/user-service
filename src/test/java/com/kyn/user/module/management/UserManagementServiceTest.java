package com.kyn.user.module.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kyn.user.base.enums.Role;
import com.kyn.user.module.authentication.repository.UserAuthRepository;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.management.dto.UserResponseDto;
import com.kyn.user.module.management.service.interfaces.UserManagementService;
import com.kyn.user.module.user.repository.UserInfoRepository;
import com.kyn.user.module.user.service.interfaces.UserSearchService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class UserManagementServiceTest {

    @Autowired
    private UserManagementService userManagementService;
    
    @Autowired
    private UserSearchService userSearchService;
    
    @Autowired
    private UserAuthRepository userAuthRepository;
    
    @Autowired
    private UserInfoRepository userInfoRepository;
    
    private static final String TEST_USER_ID = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private List<UserRequestDto> randomUsers;
    private Random random = new Random();
    
    @BeforeEach
    void setUp() {
        // Clear data before running tests (synchronous processing)
        userAuthRepository.deleteAll().block();
        userInfoRepository.deleteAll().block();
        
        // Load random user data
        randomUsers = createRandomUsers(3).collectList().block();
    }
    
    /**
     * Method to create N random users
     */
    private Flux<UserRequestDto> createRandomUsers(int count) {
        List<UserRequestDto> users = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            UserRequestDto user = UserRequestDto.builder()
                    .userId("user" + i)
                    .userName("User " + i)
                    .email("user" + i + "@example.com")
                    .password("password" + i)
                    .role(Role.USER)
                    .build();
            users.add(user);
        }
        
        return Flux.fromIterable(users);
    }
    
    @Test
    @DisplayName("Test user creation")
    void testCreateUser() {
        // Create test user request
        UserRequestDto userRequest = UserRequestDto.builder()
                .userId(TEST_USER_ID)
                .userName("Test User")
                .email(TEST_EMAIL)
                .password("password123")
                .role(Role.USER)
                .build();
        
        // Test user creation
        StepVerifier.create(userManagementService.createUser(userRequest))
            .assertNext(userDto -> {
                assert userDto.getUserInfoId() != null;
                assert userDto.getUserId().equals(TEST_USER_ID);
                assert userDto.getUserName().equals("Test User");
                assert userDto.getEmail().equals(TEST_EMAIL);
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Test user information update")
    void testUpdateUser() {
        // First, create a user
        UserResponseDto createdUser = createTestUser();
        
        // User information to update
        UserRequestDto updatedUserRequest = UserRequestDto.builder()
                .userInfoId(createdUser.getUserInfoId())
                .userId(TEST_USER_ID)
                .userName("Updated Name")
                .email("updated@example.com")
                .password("newpassword")
                .role(Role.USER)
                .build();
        
        // Test user information update
        StepVerifier.create(userManagementService.updateUser(updatedUserRequest))
            .assertNext(updatedUser -> {
                // Check UserInfoId if not null, as it might not be returned depending on the implementation
                if (updatedUser.getUserInfoId() != null) {
                    assert updatedUser.getUserInfoId().equals(createdUser.getUserInfoId());
                }
                
                // Check if name and email are updated
                assert updatedUser.getUserName().equals("Updated Name");
                assert updatedUser.getEmail().equals("updated@example.com");
            })
            .verifyComplete();
        
        // Verify if the updated information is actually saved
        StepVerifier.create(userSearchService.findUserById(createdUser.getUserInfoId()))
            .assertNext(response -> {
                assert response.getUserName().equals("Updated Name");
                assert response.getEmail().equals("updated@example.com");
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Test creation of multiple users")
    void testCreateMultipleUsers() {
        // Create random users sequentially
        List<Mono<UserResponseDto>> creationResults = new ArrayList<>();
        
        for (UserRequestDto userRequest : randomUsers) {
            creationResults.add(userManagementService.createUser(userRequest));
        }
        
        // Create a Flux combining all creation results
        Flux<UserResponseDto> allCreations = Flux.concat(creationResults);
        
        // Verify creation results
        StepVerifier.create(allCreations)
            .expectNextCount(randomUsers.size())
            .verifyComplete();
        
        // Verify if all created users can be retrieved
        for (int i = 0; i < randomUsers.size(); i++) {
            final int index = i;
            StepVerifier.create(userSearchService.findUserByEmail(randomUsers.get(i).getEmail()))
                .assertNext(response -> {
                    assert response.getUserId().equals(randomUsers.get(index).getUserId());
                    assert response.getEmail().equals(randomUsers.get(index).getEmail());
                })
                .verifyComplete();
        }
    }
    
    @Test
    @DisplayName("Test finding user by email")
    void testFindUserByEmail() {
        // Create test user
        createTestUser();
        
        // Find user by email
        StepVerifier.create(userSearchService.findUserByEmail(TEST_EMAIL))
            .assertNext(response -> {
                assert response.getUserId().equals(TEST_USER_ID);
                assert response.getEmail().equals(TEST_EMAIL);
                assert response.getUserAuths() != null;
                assert !response.getUserAuths().isEmpty();
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Test finding user by userId")
    void testFindUserByUserId() {
        // Create test user
        createTestUser();
        
        // Find user by userId
        StepVerifier.create(userSearchService.findUserByUserId(TEST_USER_ID))
            .assertNext(response -> {
                assert response.getUserId().equals(TEST_USER_ID);
                assert response.getEmail().equals(TEST_EMAIL);
            })
            .verifyComplete();
    }
    
    /**
     * Helper method to create a test user
     */
    private UserResponseDto createTestUser() {
        UserRequestDto userRequest = UserRequestDto.builder()
                .userId(TEST_USER_ID)
                .userName("Test User")
                .email(TEST_EMAIL)
                .password("password123")
                .role(Role.USER)
                .build();
        
        return userManagementService.createUser(userRequest).block();
    }
} 
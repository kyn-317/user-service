package com.kyn.user.module.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.user.module.authentication.dto.AuthenticationUserDto;
import com.kyn.user.module.management.dto.UserRequestDto;
import com.kyn.user.module.management.dto.UserResponseDto;
import com.kyn.user.module.user.mapper.UserSearchDtoMapper;
import com.kyn.user.module.user.service.interfaces.UserSearchService;
import com.kyn.user.module.user.repository.UserInfoRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserSearchServiceImpl implements UserSearchService {

    private final UserInfoRepository userInfoRepository;

    public UserSearchServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public Mono<UserResponseDto> findUserById(UUID id) {
        var dto  = UserRequestDto.builder().userInfoId(id).build();
        return findUserByDto(dto);
    }

    @Override
    public Mono<UserResponseDto> findUserByEmail(String email) {
        var dto  = UserRequestDto.builder().email(email).build();
        return findUserByDto(dto);
    }

    @Override
    public Mono<UserResponseDto> findUserByUserId(String userId) {
        var dto  = UserRequestDto.builder().userId(userId).build();
        return findUserByDto(dto);
    }

    @Override
    public Mono<UserResponseDto> findUserByUserInfoId(UUID userInfoId) {
        var dto  = UserRequestDto.builder().userInfoId(userInfoId).build();
        return findUserByDto(dto);
    }

    @Override
    public Mono<UserResponseDto> findUserByUserName(String userName) {
        var dto  = UserRequestDto.builder().userName(userName).build();
        return findUserByDto(dto);
    }

    @Override
    public Mono<UserResponseDto> findUserByDto(UserRequestDto dto) {
        return Mono.just(dto)
            .filterWhen(this::validationSearchCondition)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid Search Condition")))
            .flatMapMany(requestDto -> userInfoRepository.findByUserIdWithAuth(
                requestDto.getUserId(),
                requestDto.getUserInfoId(),
                requestDto.getEmail(),
                requestDto.getUserName()
            ))
            .collectList()
            .doOnNext(searchDtos -> log.info("searchDtos :{} ", searchDtos))
            .flatMap(searchDtos -> {
                if (searchDtos.isEmpty()) return Mono.empty();
                return Mono.just(UserSearchDtoMapper.toUserResponseDto.apply(searchDtos));
            });
    }

    @Override
    public Mono<AuthenticationUserDto> findLoginUser(String email) {
        var dto = UserRequestDto.builder().email(email).build();
        return Mono.just(dto)
        .filterWhen(this::validationSearchCondition)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid Search Condition")))
        .flatMapMany(requestDto -> userInfoRepository.findByUserIdWithAuthGetPassword(
            requestDto.getUserId(),
            requestDto.getUserInfoId(),
            requestDto.getEmail(),
            requestDto.getUserName()
        ))
        .collectList()
        .flatMap(searchDtos -> {
            if (searchDtos.isEmpty()) return Mono.empty();
            return Mono.just(UserSearchDtoMapper.toAuthenticationUserDto.apply(searchDtos));
        });
    }

    private Mono<Boolean> validationSearchCondition(UserRequestDto requestDto) {
        return Mono.just(
            requestDto.getUserInfoId() != null || requestDto.getUserId() != null || 
            requestDto.getEmail() != null || requestDto.getUserName() != null
        );
    }

    @Override
    public Mono<Boolean> isExistUser(String email) {
        return userInfoRepository.existsByEmail(email);
    }
}

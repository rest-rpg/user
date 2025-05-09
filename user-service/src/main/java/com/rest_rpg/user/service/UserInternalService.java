package com.rest_rpg.user.service;

import com.rest_rpg.user.api.model.UserLite;
import com.rest_rpg.user.api.model.UserWithPassword;
import com.rest_rpg.user.mapper.UserMapper;
import com.rest_rpg.user.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class UserInternalService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserWithPassword getUserByUsername(@NotBlank String username) {
        return userMapper.toUserWithPassword(userRepository.getByUsername(username));
    }

    public UserLite getUserLiteByUsername(@NotBlank String username) {
        return userMapper.toUserLite(userRepository.getByUsername(username));
    }

    public UserLite getUserById(long userId) {
        return userMapper.toUserLite(userRepository.getById(userId));
    }
}

package com.rest_rpg.user.controller;

import com.rest_rpg.user.api.model.UserAuth;
import com.rest_rpg.user.api.model.UserLite;
import com.rest_rpg.user.feign.UserInternalApi;
import com.rest_rpg.user.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class UserInternalController implements UserInternalApi {

    private final UserInternalService userInternalService;

    @Override
    public UserAuth getUserByUsername(String username) {
        return userInternalService.getUserByUsername(username);
    }

    @Override
    public UserLite getUserById(long userId) {
        return userInternalService.getUserById(userId);
    }
}

package com.rest_rpg.user.controller;

import com.rest_rpg.user.api.model.Role;
import com.rest_rpg.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.UserApi;
import org.openapitools.model.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController implements UserApi {

    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    @Override
    public ResponseEntity<Void> register(RegisterRequest registerRequest) {
        userService.register(registerRequest, httpServletRequest, Role.USER);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> verifyUser(String code) {
        userService.verify(code);
        return ResponseEntity.ok().build();
    }
}

package com.rest_rpg.user.controller;

import com.ms.user.api.UserApi;
import com.ms.user.model.CreateUserRequest;
import com.ms.user.model.RegisterRequest;
import com.ms.user.model.UpdatePasswordRequest;
import com.ms.user.model.UserFilter;
import com.ms.user.model.UserLite;
import com.ms.user.model.UserLitePage;
import com.ms.user.model.UserUpdateRequest;
import com.rest_rpg.user.mapper.UserMapper;
import com.rest_rpg.user.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController implements UserApi {

  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  public ResponseEntity<Void> register(RegisterRequest registerRequest) {
    userService.register(registerRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> verifyUser(String code) {
    userService.verify(code);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserLite> createUser(CreateUserRequest createUserRequest) {
    return ResponseEntity.ok(
        userMapper.toUserLite(userService.createUser(createUserRequest, true)));
  }

  @Override
  public ResponseEntity<Void> deleteUser(UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserLitePage> findUsers(UserFilter filter, Pageable pageable) {
    return ResponseEntity.ok(userService.findUsers(filter, pageable));
  }

  @Override
  public ResponseEntity<UserLite> getUser(UUID userId) {
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @Override
  public ResponseEntity<UserLite> updateUser(UUID userId, UserUpdateRequest userUpdateRequest) {
    return ResponseEntity.ok(userService.updateUserByAdmin(userId, userUpdateRequest));
  }

  @Override
  public ResponseEntity<Void> updateUserPassword(
      UUID userId, UpdatePasswordRequest updatePasswordRequest) {
    userService.updateUserPasswordByAdmin(userId, updatePasswordRequest);
    return ResponseEntity.noContent().build();
  }
}

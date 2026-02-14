package com.rest_rpg.user.controller;

import com.ms.user.api.MeApi;
import com.ms.user.model.UpdateOwnAccountRequest;
import com.ms.user.model.UpdateOwnPasswordRequest;
import com.ms.user.model.UserLite;
import com.rest_rpg.user.helper.ContextHelper;
import com.rest_rpg.user.service.UserInternalService;
import com.rest_rpg.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class MeController implements MeApi {

  private final UserInternalService userInternalService;
  private final UserService userService;

  @Override
  public ResponseEntity<UserLite> getOwnAccountInfo() {
    String username = ContextHelper.getUsernameFromContext();
    return ResponseEntity.ok(userInternalService.getUserLiteByUsername(username));
  }

  @Override
  public ResponseEntity<UserLite> updateOwnAccount(@Valid UpdateOwnAccountRequest request) {
    String username = ContextHelper.getUsernameFromContext();
    return ResponseEntity.ok(userService.updateOwnAccount(username, request));
  }

  @Override
  public ResponseEntity<Void> updateOwnPassword(
      @Valid UpdateOwnPasswordRequest updateOwnPasswordRequest) {
    String username = ContextHelper.getUsernameFromContext();
    userService.updateOwnPassword(username, updateOwnPasswordRequest);
    return ResponseEntity.noContent().build();
  }
}

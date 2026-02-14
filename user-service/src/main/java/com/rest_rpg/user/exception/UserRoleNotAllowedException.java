package com.rest_rpg.user.exception;

import com.ms.user.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserRoleNotAllowedException extends ResponseStatusException {

  public UserRoleNotAllowedException() {
    super(HttpStatus.FORBIDDEN, ErrorCodes.USER_ROLE_NOT_ALLOWED.toString());
  }
}

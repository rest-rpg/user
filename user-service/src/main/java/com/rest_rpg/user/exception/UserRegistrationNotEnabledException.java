package com.rest_rpg.user.exception;

import com.ms.user.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserRegistrationNotEnabledException extends ResponseStatusException {

  public UserRegistrationNotEnabledException() {
    super(HttpStatus.FORBIDDEN, ErrorCodes.USER_REGISTRATION_NOT_ENABLED.toString());
  }
}

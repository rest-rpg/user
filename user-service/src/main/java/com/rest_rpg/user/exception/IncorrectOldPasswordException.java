package com.rest_rpg.user.exception;

import com.ms.user.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IncorrectOldPasswordException extends ResponseStatusException {

  public IncorrectOldPasswordException() {
    super(HttpStatus.BAD_REQUEST, ErrorCodes.INCORRECT_OLD_PASSWORD.toString());
  }
}

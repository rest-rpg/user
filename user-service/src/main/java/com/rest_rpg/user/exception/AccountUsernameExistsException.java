package com.rest_rpg.user.exception;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountUsernameExistsException extends ResponseStatusException {

    public AccountUsernameExistsException() {
        super(HttpStatus.FORBIDDEN, ErrorCodes.ACCOUNT_USERNAME_EXISTS.toString());
    }
}

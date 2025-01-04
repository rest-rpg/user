package com.rest_rpg.user.exception;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyVerifiedException extends ResponseStatusException {

    public UserAlreadyVerifiedException() {
        super(HttpStatus.FORBIDDEN, ErrorCodes.ACCOUNT_ALREADY_VERIFIED.toString());
    }
}

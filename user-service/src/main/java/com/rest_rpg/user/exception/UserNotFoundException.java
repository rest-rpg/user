package com.rest_rpg.user.exception;

import org.openapitools.model.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCodes.USER_NOT_FOUND.toString());
    }
}

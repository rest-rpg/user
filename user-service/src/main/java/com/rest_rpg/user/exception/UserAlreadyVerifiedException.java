package com.rest_rpg.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyVerifiedException extends ResponseStatusException {

    public UserAlreadyVerifiedException() {
        super(HttpStatus.FORBIDDEN, "ACCOUNT_ALREADY_VERIFIED");
    }
}

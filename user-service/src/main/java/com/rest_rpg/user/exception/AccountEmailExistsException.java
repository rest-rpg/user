package com.rest_rpg.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountEmailExistsException extends ResponseStatusException {

    public AccountEmailExistsException() {
        super(HttpStatus.FORBIDDEN, "ACCOUNT_EMAIL_EXISTS");
    }
}

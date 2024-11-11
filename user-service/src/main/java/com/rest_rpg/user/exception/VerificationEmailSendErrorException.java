package com.rest_rpg.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class VerificationEmailSendErrorException extends ResponseStatusException {

    public VerificationEmailSendErrorException() {
        super(HttpStatus.FORBIDDEN, "VERIFICATION_EMAIL_SEND_ERROR");
    }
}

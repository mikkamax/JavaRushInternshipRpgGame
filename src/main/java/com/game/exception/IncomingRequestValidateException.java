package com.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncomingRequestValidateException extends RuntimeException {

    public IncomingRequestValidateException() {
    }

    public IncomingRequestValidateException(String message) {
        super(message);
    }
}

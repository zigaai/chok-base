package com.zigaai.core.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginException extends AuthenticationException {

    public LoginException(String msg) {
        super(msg);
    }

}

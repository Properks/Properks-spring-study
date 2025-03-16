package com.example.springrestdocs.config.jwt.exception;


import com.example.springrestdocs.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(JwtErrorCode code) {
        super(code);
    }
}
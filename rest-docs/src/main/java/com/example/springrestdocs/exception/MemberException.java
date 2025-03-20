package com.example.springrestdocs.exception;


import com.example.springrestdocs.apiPayload.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(MemberErrorCode code) {
        super(code);
    }
}

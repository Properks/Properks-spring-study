package com.example.springrestdocs.apiPayload.exception.base;


import com.example.springrestdocs.apiPayload.code.BaseErrorCode;
import com.example.springrestdocs.apiPayload.exception.GeneralException;

public class FailureException extends GeneralException {

    public FailureException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}

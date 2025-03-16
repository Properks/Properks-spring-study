package com.example.springrestdocs.exception;

import com.example.springrestdocs.apiPayload.exception.GeneralException;

public class ArticleException extends GeneralException {

    public ArticleException(ArticleErrorCode code) {
        super(code);
    }
}
